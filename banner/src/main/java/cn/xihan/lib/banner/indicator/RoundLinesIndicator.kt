package cn.xihan.lib.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:20
 * @介绍 :
 */
class RoundLinesIndicator(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    BaseIndicator(context, attrs, defStyleAttr) {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count: Int = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        setMeasuredDimension(config.getSelectedWidth() * count, config.getHeight())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count: Int = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        mPaint.color = config.getNormalColor()
        val oval = RectF(0.toFloat(), 0.toFloat(), canvas.width.toFloat(), config.getHeight().toFloat())
        canvas.drawRoundRect(oval, config.getRadius().toFloat(), config.getRadius().toFloat(), mPaint)
        mPaint.color = config.getSelectedColor()
        val left: Int = config.getCurrentPosition() * config.getSelectedWidth()
        val rectF = RectF(left.toFloat(), 0.toFloat(), left + (config.getSelectedWidth()).toFloat(), config.getHeight().toFloat())
        canvas.drawRoundRect(rectF, config.getRadius().toFloat(), config.getRadius().toFloat(), mPaint)
    }

    init {
        mPaint.style = Paint.Style.FILL
    }
}
