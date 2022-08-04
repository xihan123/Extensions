package cn.xihan.lib.banner.config

import androidx.annotation.ColorInt
import androidx.annotation.IntDef


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:00
 * @介绍 :
 */
class IndicatorConfig {
    private var indicatorSize = 0
    private var currentPosition = 0
    private var gravity = Direction.CENTER
    private var indicatorSpace: Int = BannerConfig.INDICATOR_SPACE
    private var normalWidth: Int = BannerConfig.INDICATOR_NORMAL_WIDTH
    private var selectedWidth: Int = BannerConfig.INDICATOR_SELECTED_WIDTH

    @ColorInt
    private var normalColor: Int = BannerConfig.INDICATOR_NORMAL_COLOR

    @ColorInt
    private var selectedColor: Int = BannerConfig.INDICATOR_SELECTED_COLOR
    private var radius: Int = BannerConfig.INDICATOR_RADIUS
    private var height: Int = BannerConfig.INDICATOR_HEIGHT
    private var margins: Margins? = null

    //是将指示器添加到banner上
    private var attachToBanner = true

    @IntDef(Direction.LEFT, Direction.CENTER, Direction.RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Direction {
        companion object {
            const val LEFT = 0
            const val CENTER = 1
            const val RIGHT = 2
        }
    }

    class Margins(
        val leftMargin: Int,
        val topMargin: Int,
        val rightMargin: Int,
        val bottomMargin: Int
    ) {
        @JvmOverloads
        constructor(marginSize: Int = BannerConfig.INDICATOR_MARGIN) : this(
            marginSize,
            marginSize,
            marginSize,
            marginSize
        )
    }

    fun getMargins(): Margins? {
        if (margins == null) {
            setMargins(Margins())
        }
        return margins
    }

    fun setMargins(margins: Margins): IndicatorConfig {
        this.margins = margins
        return this
    }

    fun getIndicatorSize(): Int {
        return indicatorSize
    }

    fun setIndicatorSize(indicatorSize: Int): IndicatorConfig {
        this.indicatorSize = indicatorSize
        return this
    }

    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(normalColor: Int): IndicatorConfig {
        this.normalColor = normalColor
        return this
    }

    fun getSelectedColor(): Int {
        return selectedColor
    }

    fun setSelectedColor(selectedColor: Int): IndicatorConfig {
        this.selectedColor = selectedColor
        return this
    }

    fun getIndicatorSpace(): Int {
        return indicatorSpace
    }

    fun setIndicatorSpace(indicatorSpace: Int): IndicatorConfig {
        this.indicatorSpace = indicatorSpace
        return this
    }

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun setCurrentPosition(currentPosition: Int): IndicatorConfig {
        this.currentPosition = currentPosition
        return this
    }

    fun getNormalWidth(): Int {
        return normalWidth
    }

    fun setNormalWidth(normalWidth: Int): IndicatorConfig {
        this.normalWidth = normalWidth
        return this
    }

    fun getSelectedWidth(): Int {
        return selectedWidth
    }

    fun setSelectedWidth(selectedWidth: Int): IndicatorConfig {
        this.selectedWidth = selectedWidth
        return this
    }

    fun getGravity(): Int {
        return gravity
    }

    fun setGravity(@Direction gravity: Int): IndicatorConfig {
        this.gravity = gravity
        return this
    }

    fun isAttachToBanner(): Boolean {
        return attachToBanner
    }

    fun setAttachToBanner(attachToBanner: Boolean): IndicatorConfig {
        this.attachToBanner = attachToBanner
        return this
    }

    fun getRadius(): Int {
        return radius
    }

    fun setRadius(radius: Int): IndicatorConfig {
        this.radius = radius
        return this
    }

    fun getHeight(): Int {
        return height
    }

    fun setHeight(height: Int): IndicatorConfig {
        this.height = height
        return this
    }
}
