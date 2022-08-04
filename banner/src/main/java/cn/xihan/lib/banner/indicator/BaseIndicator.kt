package cn.xihan.lib.banner.indicator

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.xihan.lib.banner.config.IndicatorConfig


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:20
 * @介绍 :
 */
open class BaseIndicator(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr), Indicator {
    protected val config: IndicatorConfig = IndicatorConfig()
    protected val mPaint: Paint = Paint()
    private var offset = 0f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun getIndicatorView(): View {
        if (config.isAttachToBanner()) {
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            when (config.getGravity()) {
                IndicatorConfig.Direction.LEFT -> layoutParams.gravity =
                    Gravity.BOTTOM or Gravity.START
                IndicatorConfig.Direction.CENTER -> layoutParams.gravity =
                    Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                IndicatorConfig.Direction.RIGHT -> layoutParams.gravity =
                    Gravity.BOTTOM or Gravity.END
            }
            layoutParams.leftMargin = config.getMargins()!!.leftMargin
            layoutParams.rightMargin = config.getMargins()!!.rightMargin
            layoutParams.topMargin = config.getMargins()!!.topMargin
            layoutParams.bottomMargin = config.getMargins()!!.bottomMargin
            setLayoutParams(layoutParams)
        }
        return this
    }

    override fun getIndicatorConfig(): IndicatorConfig {
        return config
    }

    override fun onPageChanged(count: Int, currentPosition: Int) {
        config.setIndicatorSize(count)
        config.setCurrentPosition(currentPosition)
        requestLayout()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        offset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        config.setCurrentPosition(position)
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.TRANSPARENT
        mPaint.color = config.getNormalColor()
    }
}
