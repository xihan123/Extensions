package cn.xihan.lib.banner


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import cn.xihan.lib.banner.adapter.BannerAdapter
import cn.xihan.lib.banner.config.BannerConfig
import cn.xihan.lib.banner.config.IndicatorConfig
import cn.xihan.lib.banner.indicator.Indicator
import cn.xihan.lib.banner.listener.OnBannerListener
import cn.xihan.lib.banner.listener.OnPageChangeListener
import cn.xihan.lib.banner.transformer.MZScaleInTransformer
import cn.xihan.lib.banner.transformer.ScaleInTransformer
import cn.xihan.lib.banner.util.BannerLifecycleObserver
import cn.xihan.lib.banner.util.BannerLifecycleObserverAdapter
import cn.xihan.lib.banner.util.BannerUtils.dp2px
import cn.xihan.lib.banner.util.BannerUtils.getRealPosition
import cn.xihan.lib.banner.util.BannerUtils.setBannerRound
import cn.xihan.lib.banner.util.ScrollSpeedManger
import java.lang.ref.WeakReference
import kotlin.math.abs


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:36
 * @介绍 :
 */
class Banner<T, BA : BannerAdapter<T, out RecyclerView.ViewHolder>> :
    FrameLayout, BannerLifecycleObserver {



    private val mViewPager2: ViewPager2
    private val mLoopTask: AutoLoopTask
    private lateinit var mAdapter: BA
    private val mCompositePageTransformer = CompositePageTransformer()
    private var mIndicator: Indicator? = null

    val INVALID_VALUE = -1

    private var mOnPageChangeListener: OnPageChangeListener? = null

    // 是否允许无限轮播（即首尾直接切换）
    var mIsInfiniteLoop = BannerConfig.IS_INFINITE_LOOP

    // 轮播切换时间
    var mScrollTime = BannerConfig.SCROLL_TIME

    // 轮播开始位置
    var mStartPosition = 1

    // banner圆角半径，默认没有圆角
    var mBannerRadius = 0f

    // 是否自动轮播
    var mIsAutoLoop = BannerConfig.IS_AUTO_LOOP

    // 轮播切换间隔时间
    var mLoopTime = BannerConfig.LOOP_TIME.toLong()

    // banner圆角方向，如果一个都不设置，默认四个角全部圆角
    private var mRoundTopLeft = false  // banner圆角方向，如果一个都不设置，默认四个角全部圆角
    private var mRoundTopRight = false  // banner圆角方向，如果一个都不设置，默认四个角全部圆角
    private var mRoundBottomLeft = false  // banner圆角方向，如果一个都不设置，默认四个角全部圆角
    private var mRoundBottomRight = false

    // 指示器相关配置
    private var normalWidth = BannerConfig.INDICATOR_NORMAL_WIDTH
    private var selectedWidth = BannerConfig.INDICATOR_SELECTED_WIDTH
    private var normalColor = BannerConfig.INDICATOR_NORMAL_COLOR
    private var selectedColor = BannerConfig.INDICATOR_SELECTED_COLOR
    private var indicatorGravity = IndicatorConfig.Direction.CENTER
    private var indicatorSpace = 0
    private var indicatorMargin = 0
    private var indicatorMarginLeft = 0
    private var indicatorMarginTop = 0
    private var indicatorMarginRight = 0
    private var indicatorMarginBottom = 0
    private var indicatorHeight = BannerConfig.INDICATOR_HEIGHT
    private var indicatorRadius = BannerConfig.INDICATOR_RADIUS


    private var mOrientation = Orientation.HORIZONTAL

    // 滑动距离范围
    private var mTouchSlop = 0

    // 记录触摸的位置（主要用于解决事件冲突问题）
    private var mStartX = 0f  // 记录触摸的位置（主要用于解决事件冲突问题）
    private var mStartY = 0f

    // 记录viewpager2是否被拖动
    private var mIsViewPager2Drag = false

    // 是否要拦截事件
    private var isIntercept = true

    //绘制圆角视图
    private val mRoundPaint: Paint
    private val mImagePaint: Paint

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initTypedArray(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initTypedArray(context, attrs)
    }

    @IntDef(Orientation.HORIZONTAL, Orientation.VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Orientation {
        companion object {
            const val HORIZONTAL = 0
            const val VERTICAL = 1
        }

    }

    var mPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            private var mTempPosition: Int = INVALID_VALUE
            private var isScrolled = false

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val realPosition = getRealPosition(isInfiniteLoop(), position, getRealCount())
                if (mOnPageChangeListener != null && realPosition == getCurrentItem() - 1) {
                    mOnPageChangeListener!!.onPageScrolled(
                        realPosition,
                        positionOffset,
                        positionOffsetPixels
                    )
                }
                if (getIndicator() != null && realPosition == getCurrentItem() - 1) {
                    getIndicator()?.onPageScrolled(
                        realPosition,
                        positionOffset,
                        positionOffsetPixels
                    )
                }
            }

            override fun onPageSelected(position: Int) {
                if (isScrolled) {
                    mTempPosition = position
                    val realPosition = getRealPosition(isInfiniteLoop(), position, getRealCount())
                    mOnPageChangeListener?.onPageSelected(realPosition)
                    if (getIndicator() != null) {
                        getIndicator()?.onPageSelected(realPosition)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //手势滑动中,代码执行滑动中
                if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                    isScrolled = true
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //滑动闲置或滑动结束
                    isScrolled = false
                    if (mTempPosition != INVALID_VALUE && mIsInfiniteLoop) {
                        if (mTempPosition == 0) {
                            setCurrentItem(getRealCount(), false)
                        } else if (mTempPosition == getItemCount() - 1) {
                            setCurrentItem(1, false)
                        }
                    }
                }
                mOnPageChangeListener?.onPageScrollStateChanged(state)
                if (getIndicator() != null) {
                    getIndicator()?.onPageScrollStateChanged(state)
                }
            }
        }


    private fun initIndicatorAttr() {
        if (indicatorMargin != 0) {
            setIndicatorMargins(IndicatorConfig.Margins(indicatorMargin))
        } else if (indicatorMarginLeft != 0 || indicatorMarginTop != 0 || indicatorMarginRight != 0 || indicatorMarginBottom != 0) {
            setIndicatorMargins(
                IndicatorConfig.Margins(
                    indicatorMarginLeft,
                    indicatorMarginTop,
                    indicatorMarginRight,
                    indicatorMarginBottom
                )
            )
        }
        if (indicatorSpace > 0) {
            setIndicatorSpace(indicatorSpace)
        }
        if (indicatorGravity != IndicatorConfig.Direction.CENTER) {
            setIndicatorGravity(indicatorGravity)
        }
        if (normalWidth > 0) {
            setIndicatorNormalWidth(normalWidth)
        }
        if (selectedWidth > 0) {
            setIndicatorSelectedWidth(selectedWidth)
        }
        if (indicatorHeight > 0) {
            setIndicatorHeight(indicatorHeight)
        }
        if (indicatorRadius > 0) {
            setIndicatorRadius(indicatorRadius)
        }
        setIndicatorNormalColor(normalColor)
        setIndicatorSelectedColor(selectedColor)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!getViewPager2().isUserInputEnabled) {
            return super.dispatchTouchEvent(ev)
        }
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            start()
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!getViewPager2().isUserInputEnabled || !isIntercept) {
            return super.onInterceptTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val endY = event.y
                val distanceX = abs(endX - mStartX)
                val distanceY = abs(endY - mStartY)
                mIsViewPager2Drag =
                    if (getViewPager2().orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                        distanceX > mTouchSlop && distanceX > distanceY
                    } else {
                        distanceY > mTouchSlop && distanceY > distanceX
                    }
                parent.requestDisallowInterceptTouchEvent(mIsViewPager2Drag)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (mBannerRadius > 0) {
            canvas.saveLayer(
                RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat()),
                mImagePaint,
                Canvas.ALL_SAVE_FLAG
            )
            super.dispatchDraw(canvas)
            //绘制外圆环边框圆环
            //默认四个角都设置
            if (!mRoundTopRight && !mRoundTopLeft && !mRoundBottomRight && !mRoundBottomLeft) {
                drawTopLeft(canvas)
                drawTopRight(canvas)
                drawBottomLeft(canvas)
                drawBottomRight(canvas)
                canvas.restore()
                return
            }
            if (mRoundTopLeft) {
                drawTopLeft(canvas)
            }
            if (mRoundTopRight) {
                drawTopRight(canvas)
            }
            if (mRoundBottomLeft) {
                drawBottomLeft(canvas)
            }
            if (mRoundBottomRight) {
                drawBottomRight(canvas)
            }
            canvas.restore()
        } else {
            super.dispatchDraw(canvas)
        }
    }

    private fun drawTopLeft(canvas: Canvas) {
        val path = Path()
        path.moveTo(0F, mBannerRadius)
        path.lineTo(0F, 0F)
        path.lineTo(mBannerRadius, 0F)
        path.arcTo(RectF(0F, 0F, mBannerRadius * 2, mBannerRadius * 2), -90F, -90F)
        path.close()
        canvas.drawPath(path, mRoundPaint)
    }

    private fun drawTopRight(canvas: Canvas) {
        val width = width
        val path = Path()
        path.moveTo(width - mBannerRadius, 0F)
        path.lineTo(width.toFloat(), 0F)
        path.lineTo(width.toFloat(), mBannerRadius)
        path.arcTo(
            RectF(width - 2 * mBannerRadius, 0F, width.toFloat(), mBannerRadius * 2),
            0F,
            -90F
        )
        path.close()
        canvas.drawPath(path, mRoundPaint)
    }

    private fun drawBottomLeft(canvas: Canvas) {
        val height = height
        val path = Path()
        path.moveTo(0F, height - mBannerRadius)
        path.lineTo(0F, height.toFloat())
        path.lineTo(mBannerRadius, height.toFloat())
        path.arcTo(
            RectF(0F, height - 2 * mBannerRadius, mBannerRadius * 2, height.toFloat()),
            90F,
            90F
        )
        path.close()
        canvas.drawPath(path, mRoundPaint)
    }

    private fun drawBottomRight(canvas: Canvas) {
        val height = height
        val width = width
        val path = Path()
        path.moveTo(width - mBannerRadius, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), height - mBannerRadius)
        path.arcTo(
            RectF(
                width - 2 * mBannerRadius, height - 2 * mBannerRadius, width.toFloat(),
                height.toFloat()
            ), 0F, 90F
        )
        path.close()
        canvas.drawPath(path, mRoundPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }


    internal class AutoLoopTask(banner: Banner<*, *>) : Runnable {
        private val reference: WeakReference<Banner<*, *>>
        override fun run() {
            val banner = reference.get()
            if (banner != null && banner.mIsAutoLoop) {
                val count: Int = banner.getItemCount()
                if (count == 0) {
                    return
                }
                val next: Int = (banner.getCurrentItem() + 1) % count
                banner.setCurrentItem(next)
                banner.postDelayed(banner.mLoopTask, banner.mLoopTime)
            }
        }

        init {
            reference = WeakReference(banner)
        }
    }


    private val mAdapterDataObserver: RecyclerView.AdapterDataObserver =
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (getItemCount() <= 1) {
                    stop()
                } else {
                    start()
                }
                setIndicatorPageChange()
            }
        }

    private fun initIndicator() {
        if (getIndicator() == null) {
            return
        }
        if (getIndicator()?.getIndicatorConfig()?.isAttachToBanner() == true) {
            removeIndicator()
            addView(getIndicator()?.getIndicatorView())
        }
        initIndicatorAttr()
        setIndicatorPageChange()
    }

    private fun setInfiniteLoop() {
        // 当不支持无限循环时，要关闭自动轮播
        if (!isInfiniteLoop()) {
            isAutoLoop(false)
        }
        setStartPosition(if (isInfiniteLoop()) mStartPosition else 0)
    }

    private fun setRecyclerViewPadding(itemPadding: Int) {
        setRecyclerViewPadding(itemPadding, itemPadding)
    }

    private fun setRecyclerViewPadding(leftItemPadding: Int, rightItemPadding: Int) {
        val recyclerView = getViewPager2().getChildAt(0) as RecyclerView
        if (getViewPager2().orientation == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(
                mViewPager2.paddingLeft,
                leftItemPadding,
                mViewPager2.paddingRight,
                rightItemPadding
            )
        } else {
            recyclerView.setPadding(
                leftItemPadding,
                mViewPager2.paddingTop,
                rightItemPadding,
                mViewPager2.paddingBottom
            )
        }
        recyclerView.clipToPadding = false
    }

    /**
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */

    fun getCurrentItem(): Int {
        return getViewPager2().currentItem
    }

    fun getItemCount(): Int {
        return if (getAdapter() != null) {
            getAdapter().itemCount
        } else 0
    }

    fun getScrollTime(): Int {
        return mScrollTime
    }

    fun isInfiniteLoop(): Boolean {
        return mIsInfiniteLoop
    }

    fun getAdapter(): BannerAdapter<*, *> {
        return mAdapter
    }

    fun getViewPager2(): ViewPager2 {
        return mViewPager2
    }

    fun getIndicator(): Indicator? {
        return mIndicator
    }

    fun getIndicatorConfig(): IndicatorConfig? {
        return if (getIndicator() != null) {
            getIndicator()!!.getIndicatorConfig()
        } else null
    }

    /**
     * 返回banner真实总数
     */
    fun getRealCount(): Int {
        return getAdapter().getRealCount()
    }

    //-----------------------------------------------------------------------------------------

    /**
     * 是否要拦截事件
     * @param intercept
     * @return
     */
    fun setIntercept(intercept: Boolean): Banner<*, *> {
        isIntercept = intercept
        return this
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     * @param position
     * @return
     */
    fun setCurrentItem(position: Int): Banner<*, *> {
        return setCurrentItem(position, true)
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     * @param position
     * @param smoothScroll
     * @return
     */
    fun setCurrentItem(position: Int, smoothScroll: Boolean): Banner<*, *> {
        getViewPager2().setCurrentItem(position, smoothScroll)
        return this
    }

    fun setIndicatorPageChange(): Banner<*, *> {
        if (getIndicator() != null) {
            val realPosition = getRealPosition(isInfiniteLoop(), getCurrentItem(), getRealCount())
            getIndicator()!!.onPageChanged(getRealCount(), realPosition)
        }
        return this
    }

    fun removeIndicator(): Banner<*, *> {
        if (getIndicator() != null) {
            removeView(getIndicator()!!.getIndicatorView())
        }
        return this
    }

    /**
     * 设置开始的位置 (需要在setAdapter或者setDatas之前调用才有效哦)
     */
    open fun setStartPosition(startPosition: Int): Banner<*, *> {
        mStartPosition = startPosition
        return this
    }

    fun getStartPosition(): Int {
        return mStartPosition
    }

    /**
     * 禁止手动滑动
     *
     * @param enabled true 允许，false 禁止
     */
    fun setUserInputEnabled(enabled: Boolean): Banner<*, *> {
        getViewPager2().isUserInputEnabled = enabled
        return this
    }

    /**
     * 添加PageTransformer，可以组合效果
     * [ViewPager2.PageTransformer]
     * 如果找不到请导入implementation "androidx.viewpager2:viewpager2:1.0.0"
     */
    fun addPageTransformer(transformer: ViewPager2.PageTransformer): Banner<*, *> {
        mCompositePageTransformer.addTransformer(transformer)
        return this
    }

    /**
     * 设置PageTransformer，和addPageTransformer不同，这个只支持一种transformer
     */
    fun setPageTransformer(transformer: ViewPager2.PageTransformer): Banner<*, *> {
        getViewPager2().setPageTransformer(transformer)
        return this
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer): Banner<*, *> {
        mCompositePageTransformer.removeTransformer(transformer)
        return this
    }

    /**
     * 添加 ItemDecoration
     */
    fun addItemDecoration(decor: RecyclerView.ItemDecoration): Banner<*, *> {
        getViewPager2().addItemDecoration(decor)
        return this
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int): Banner<*, *> {
        getViewPager2().addItemDecoration(decor, index)
        return this
    }

    /**
     * 是否允许自动轮播
     *
     * @param isAutoLoop ture 允许，false 不允许
     */
    fun isAutoLoop(isAutoLoop: Boolean): Banner<*, *> {
        mIsAutoLoop = isAutoLoop
        return this
    }


    /**
     * 设置轮播间隔时间
     *
     * @param loopTime 时间（毫秒）
     */
    fun setLoopTime(loopTime: Long): Banner<*, *> {
        mLoopTime = loopTime
        return this
    }

    /**
     * 设置轮播滑动过程的时间
     */
    fun setScrollTime(scrollTime: Int): Banner<*, *> {
        mScrollTime = scrollTime
        return this
    }

    /**
     * 开始轮播
     */
    fun start(): Banner<*, *> {
        if (mIsAutoLoop) {
            stop()
            postDelayed(mLoopTask, mLoopTime)
        }
        return this
    }

    /**
     * 停止轮播
     */
    fun stop(): Banner<*, *> {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask)
        }
        return this
    }

    /**
     * 移除一些引用
     */
    fun destroy() {
        getViewPager2().unregisterOnPageChangeCallback(mPageChangeCallback)
        stop()
    }

    /**
     * 设置banner的适配器
     */
    fun setAdapter(adapter: BA?): Banner<*, *> {
        if (adapter == null) {
            throw NullPointerException(context.getString(R.string.banner_adapter_null_error))
        }
        mAdapter = adapter
        if (!isInfiniteLoop()) {
            getAdapter().setIncreaseCount(0)
        }
        getAdapter().registerAdapterDataObserver(mAdapterDataObserver)
        mViewPager2.adapter = adapter
        setCurrentItem(mStartPosition, false)
        initIndicator()
        return this
    }

    /**
     * 设置banner的适配器
     * @param adapter
     * @param isInfiniteLoop 是否支持无限循环
     * @return
     */
    fun setAdapter(adapter: BA, isInfiniteLoop: Boolean): Banner<*, *> {
        mIsInfiniteLoop = isInfiniteLoop
        setInfiniteLoop()
        setAdapter(adapter)
        return this
    }

    /**
     * 重新设置banner数据，当然你也可以在你adapter中自己操作数据,不要过于局限在这个方法，举一反三哈
     *
     * @param datas 数据集合，当传null或者datas没有数据时，banner会变成空白的，请做好占位UI处理
     */
    fun setDatas(datas: MutableList<T>): Banner<*, *> {
        if (this::mAdapter.isInitialized) {
            mAdapter.setDatas(datas)
        }
        setCurrentItem(mStartPosition, false)
        setIndicatorPageChange()
        start()
        return this
    }

    /**
     * 设置banner轮播方向
     *
     * @param orientation [Orientation]
     */
    fun setOrientation(@Orientation orientation: Int): Banner<*, *> {
        getViewPager2().orientation = orientation
        return this
    }

    /**
     * 改变最小滑动距离
     */
    fun setTouchSlop(mTouchSlop: Int): Banner<*, *> {
        this.mTouchSlop = mTouchSlop
        return this
    }

    /**
     * 设置点击事件
     */
    fun setOnBannerListener(listener: OnBannerListener<T>): Banner<*, *> {
        if (this::mAdapter.isInitialized) {
            mAdapter.setOnBannerListener(listener)
        }
        return this
    }

    /**
     * 添加viewpager切换事件
     *
     *
     * 在viewpager2中切换事件[ViewPager2.OnPageChangeCallback]是一个抽象类，
     * 为了方便使用习惯这里用的是和viewpager一样的[ViewPager.OnPageChangeListener]接口
     *
     */
    fun addOnPageChangeListener(pageListener: OnPageChangeListener): Banner<*, *> {
        mOnPageChangeListener = pageListener
        return this
    }

    /**
     * 设置banner圆角
     *
     *
     * 默认没有圆角，需要取消圆角把半径设置为0即可
     *
     * @param radius 圆角半径
     */
    fun setBannerRound(radius: Float): Banner<*, *> {
        mBannerRadius = radius
        return this
    }

    /**
     * 设置banner圆角(第二种方式，和上面的方法不要同时使用)，只支持5.0以上
     */
    fun setBannerRound2(radius: Float): Banner<*, *> {
        setBannerRound(this, radius)
        return this
    }

    /**
     * 为banner添加画廊效果
     *
     * @param itemWidth  item左右展示的宽度,单位dp
     * @param pageMargin 页面间距,单位dp
     */
    fun setBannerGalleryEffect(itemWidth: Int, pageMargin: Int): Banner<*, *> {
        return setBannerGalleryEffect(itemWidth, pageMargin, .85f)
    }

    /**
     * 为banner添加画廊效果
     *
     * @param leftItemWidth  item左展示的宽度,单位dp
     * @param rightItemWidth item右展示的宽度,单位dp
     * @param pageMargin     页面间距,单位dp
     */
    fun setBannerGalleryEffect(
        leftItemWidth: Int,
        rightItemWidth: Int,
        pageMargin: Int
    ): Banner<*, *> {
        return setBannerGalleryEffect(leftItemWidth, rightItemWidth, pageMargin, .85f)
    }

    /**
     * 为banner添加画廊效果
     *
     * @param itemWidth  item左右展示的宽度,单位dp
     * @param pageMargin 页面间距,单位dp
     * @param scale      缩放[0-1],1代表不缩放
     */
    fun setBannerGalleryEffect(itemWidth: Int, pageMargin: Int, scale: Float): Banner<*, *> {
        return setBannerGalleryEffect(itemWidth, itemWidth, pageMargin, scale)
    }

    /**
     * 为banner添加画廊效果
     *
     * @param leftItemWidth  item左展示的宽度,单位dp
     * @param rightItemWidth item右展示的宽度,单位dp
     * @param pageMargin     页面间距,单位dp
     * @param scale          缩放[0-1],1代表不缩放
     */
    fun setBannerGalleryEffect(
        leftItemWidth: Int,
        rightItemWidth: Int,
        pageMargin: Int,
        scale: Float
    ): Banner<*, *> {
        if (pageMargin > 0) {
            addPageTransformer(MarginPageTransformer(dp2px(pageMargin.toFloat())))
        }
        if (scale < 1 && scale > 0) {
            addPageTransformer(ScaleInTransformer(scale))
        }
        setRecyclerViewPadding(
            if (leftItemWidth > 0) dp2px((leftItemWidth + pageMargin).toFloat()) else 0,
            if (rightItemWidth > 0) dp2px((rightItemWidth + pageMargin).toFloat()) else 0
        )
        return this
    }

    /**
     * 为banner添加魅族效果
     *
     * @param itemWidth item左右展示的宽度,单位dp
     */
    fun setBannerGalleryMZ(itemWidth: Int): Banner<*, *> {
        return setBannerGalleryMZ(itemWidth, .88f)
    }

    /**
     * 为banner添加魅族效果
     *
     * @param itemWidth item左右展示的宽度,单位dp
     * @param scale     缩放[0-1],1代表不缩放
     */
    fun setBannerGalleryMZ(itemWidth: Int, scale: Float): Banner<*, *> {
        if (scale < 1 && scale > 0) {
            addPageTransformer(MZScaleInTransformer(scale))
        }
        setRecyclerViewPadding(dp2px(itemWidth.toFloat()))
        return this
    }

    /*
      **********************************************************************
      ------------------------ 指示器相关设置 --------------------------------*
      **********************************************************************
     */

    /*
      **********************************************************************
      ------------------------ 指示器相关设置 --------------------------------*
      **********************************************************************
     */
    /**
     * 设置轮播指示器(显示在banner上)
     */
    fun setIndicator(indicator: Indicator): Banner<*, *> {
        return setIndicator(indicator, true)
    }

    /**
     * 设置轮播指示器(如果你的指示器写在布局文件中，attachToBanner传false)
     *
     * @param attachToBanner 是否将指示器添加到banner中，false 代表你可以将指示器通过布局放在任何位置
     * 注意：设置为false后，内置的 setIndicatorGravity()和setIndicatorMargins() 方法将失效。
     * 想改变可以自己调用系统提供的属性在布局文件中进行设置。具体可以参照demo
     */
    fun setIndicator(indicator: Indicator, attachToBanner: Boolean): Banner<*, *> {
        removeIndicator()
        indicator.getIndicatorConfig().setAttachToBanner(attachToBanner)
        mIndicator = indicator
        initIndicator()
        return this
    }

    fun setIndicatorSelectedColor(@ColorInt color: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setSelectedColor(color)
        }
        return this
    }

    fun setIndicatorSelectedColorRes(@ColorRes color: Int): Banner<*, *> {
        setIndicatorSelectedColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setIndicatorNormalColor(@ColorInt color: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setNormalColor(color)
        }
        return this
    }

    fun setIndicatorNormalColorRes(@ColorRes color: Int): Banner<*, *> {
        setIndicatorNormalColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setIndicatorGravity(@IndicatorConfig.Direction gravity: Int): Banner<*, *> {
        if (getIndicatorConfig() != null && getIndicatorConfig()!!.isAttachToBanner()) {
            getIndicatorConfig()!!.setGravity(gravity)
            getIndicator()!!.getIndicatorView()!!.postInvalidate()
        }
        return this
    }

    fun setIndicatorSpace(indicatorSpace: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setIndicatorSpace(indicatorSpace)
        }
        return this
    }

    fun setIndicatorMargins(margins: IndicatorConfig.Margins): Banner<*, *> {
        if (getIndicatorConfig() != null && getIndicatorConfig()!!.isAttachToBanner()) {
            getIndicatorConfig()!!.setMargins(margins)
            getIndicator()!!.getIndicatorView()!!.requestLayout()
        }
        return this
    }

    fun setIndicatorWidth(normalWidth: Int, selectedWidth: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setNormalWidth(normalWidth)
            getIndicatorConfig()!!.setSelectedWidth(selectedWidth)
        }
        return this
    }

    fun setIndicatorNormalWidth(normalWidth: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setNormalWidth(normalWidth)
        }
        return this
    }

    fun setIndicatorSelectedWidth(selectedWidth: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setSelectedWidth(selectedWidth)
        }
        return this
    }

    fun setIndicatorRadius(indicatorRadius: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setRadius(indicatorRadius)
        }
        return this
    }

    fun setIndicatorHeight(indicatorHeight: Int): Banner<*, *> {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig()!!.setHeight(indicatorHeight)
        }
        return this
    }

    /**
     * **********************************************************************
     * ------------------------ 生命周期控制 --------------------------------*
     * **********************************************************************
     */
    fun addBannerLifecycleObserver(owner: LifecycleOwner?): Banner<*, *> {
        owner?.lifecycle?.addObserver(BannerLifecycleObserverAdapter(owner, this))
        return this
    }

    override fun onStart(owner: LifecycleOwner?) {
        start()
    }

    override fun onStop(owner: LifecycleOwner?) {
        stop()
    }

    override fun onDestroy(owner: LifecycleOwner?) {
        destroy()
    }

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop / 2
        mLoopTask = AutoLoopTask(this)
        mViewPager2 = ViewPager2(context)
        mViewPager2.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mViewPager2.offscreenPageLimit = 2
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback)
        mViewPager2.setPageTransformer(mCompositePageTransformer)
        ScrollSpeedManger.reflectLayoutManager(this)
        addView(mViewPager2)
        mRoundPaint = Paint()
        mRoundPaint.color = Color.WHITE
        mRoundPaint.isAntiAlias = true
        mRoundPaint.style = Paint.Style.FILL
        mRoundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mImagePaint = Paint()
        mImagePaint.xfermode = null
    }

    private fun initTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.Banner)
            mBannerRadius = a.getDimensionPixelSize(R.styleable.Banner_banner_radius, 0).toFloat()
            mLoopTime =
                a.getInt(R.styleable.Banner_banner_loop_time, BannerConfig.LOOP_TIME).toLong()
            mIsAutoLoop =
                a.getBoolean(R.styleable.Banner_banner_auto_loop, BannerConfig.IS_AUTO_LOOP)
            mIsInfiniteLoop =
                a.getBoolean(R.styleable.Banner_banner_infinite_loop, BannerConfig.IS_INFINITE_LOOP)
            normalWidth = a.getDimensionPixelSize(
                R.styleable.Banner_banner_indicator_normal_width,
                BannerConfig.INDICATOR_NORMAL_WIDTH
            )
            selectedWidth = a.getDimensionPixelSize(
                R.styleable.Banner_banner_indicator_selected_width,
                BannerConfig.INDICATOR_SELECTED_WIDTH
            )
            normalColor = a.getColor(
                R.styleable.Banner_banner_indicator_normal_color,
                BannerConfig.INDICATOR_NORMAL_COLOR
            )
            selectedColor = a.getColor(
                R.styleable.Banner_banner_indicator_selected_color,
                BannerConfig.INDICATOR_SELECTED_COLOR
            )
            indicatorGravity = a.getInt(
                R.styleable.Banner_banner_indicator_gravity,
                IndicatorConfig.Direction.CENTER
            )
            indicatorSpace = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_space, 0)
            indicatorMargin = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_margin, 0)
            indicatorMarginLeft =
                a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginLeft, 0)
            indicatorMarginTop =
                a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginTop, 0)
            indicatorMarginRight =
                a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginRight, 0)
            indicatorMarginBottom =
                a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginBottom, 0)
            indicatorHeight = a.getDimensionPixelSize(
                R.styleable.Banner_banner_indicator_height,
                BannerConfig.INDICATOR_HEIGHT
            )
            indicatorRadius = a.getDimensionPixelSize(
                R.styleable.Banner_banner_indicator_radius,
                BannerConfig.INDICATOR_RADIUS
            )
            mOrientation = a.getInt(R.styleable.Banner_banner_orientation, Orientation.HORIZONTAL)
            mRoundTopLeft = a.getBoolean(R.styleable.Banner_banner_round_top_left, false)
            mRoundTopRight = a.getBoolean(R.styleable.Banner_banner_round_top_right, false)
            mRoundBottomLeft = a.getBoolean(R.styleable.Banner_banner_round_bottom_left, false)
            mRoundBottomRight = a.getBoolean(R.styleable.Banner_banner_round_bottom_right, false)
            a.recycle()
        }
        setOrientation(mOrientation)
        setInfiniteLoop()

    }

}


