package cn.xihan.lib.banner.indicator

import android.content.Context
import android.graphics.Canvas

import android.graphics.RectF

import android.util.AttributeSet




/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:25
 * @介绍 :
 */
/**
 * 矩形（条形）指示器
 * 1、可以设置选中和默认的宽度、指示器的圆角
 * 2、如果需要正方形将圆角设置为0，可将宽度和高度设置为一样
 * 3、如果不想选中时变长，可将选中的宽度和默认宽度设置为一样
 */
class RectangleIndicator(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    BaseIndicator(context, attrs, defStyleAttr) {
    private val rectF: RectF = RectF()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        //间距*（总数-1）+默认宽度*（总数-1）+选中宽度
        val space = config.getIndicatorSpace() * (count - 1)
        val normal = config.getNormalWidth() * (count - 1)
        setMeasuredDimension(space + normal + config.getSelectedWidth(), config.getHeight())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        var left = 0f
        for (i in 0 until count) {
            mPaint.color =
                if (config.getCurrentPosition() == i) config.getSelectedColor() else config.getNormalColor()
            val indicatorWidth =
                if (config.getCurrentPosition() == i) config.getSelectedWidth() else config.getNormalWidth()
            rectF[left, 0f, left + indicatorWidth] = config.getHeight().toFloat()
            left += (indicatorWidth + config.getIndicatorSpace()).toFloat()
            canvas.drawRoundRect(
                rectF,
                config.getRadius().toFloat(),
                config.getRadius().toFloat(),
                mPaint
            )
        }
    }

}
