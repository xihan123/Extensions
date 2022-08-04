package cn.xihan.lib.banner.indicator


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import cn.xihan.lib.banner.R
import kotlin.math.max


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:27
 * @介绍 : Drawable指示器
 */
class DrawableIndicator : BaseIndicator {
    private var normalBitmap: Bitmap? = null
    private var selectedBitmap: Bitmap? = null

    /**
     * 实例化Drawable指示器 ，也可以通过自定义属性设置
     * @param context
     * @param normalResId
     * @param selectedResId
     */
    constructor(
        context: Context?,
        @DrawableRes normalResId: Int,
        @DrawableRes selectedResId: Int
    ) : super(context) {
        normalBitmap = BitmapFactory.decodeResource(resources, normalResId)
        selectedBitmap = BitmapFactory.decodeResource(resources, selectedResId)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableIndicator)
        val normal =
            a.getDrawable(R.styleable.DrawableIndicator_normal_drawable) as BitmapDrawable
        val selected =
            a.getDrawable(R.styleable.DrawableIndicator_selected_drawable) as BitmapDrawable
        normalBitmap = normal.bitmap
        selectedBitmap = selected.bitmap
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.getIndicatorSize()
        if (count <= 1) {
            return
        }

        if (selectedBitmap != null && normalBitmap != null){
            setMeasuredDimension(
                selectedBitmap!!.width * (count - 1) + selectedBitmap!!.width + config.getIndicatorSpace() * (count - 1),
                max(normalBitmap!!.height, selectedBitmap!!.height)
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.getIndicatorSize()
        if (count <= 1 || normalBitmap == null || selectedBitmap == null) {
            return
        }
        var left = 0f
        for (i in 0 until count) {
            canvas.drawBitmap(
                (if (config.getCurrentPosition() == i) selectedBitmap else normalBitmap)!!,
                left,
                0f,
                mPaint
            )
            left += (normalBitmap!!.width + config.getIndicatorSpace()).toFloat()
        }
    }
}
