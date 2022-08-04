package cn.xihan.lib.banner.listener

import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:05
 * @介绍 :
 */
interface OnPageChangeListener {
    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     * Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    fun onPageSelected(position: Int)

    /**
     * Called when the scroll state changes. Useful for discovering when the user begins
     * dragging, when a fake drag is started, when the pager is automatically settling to the
     * current page, or when it is fully stopped/idle. `state` can be one of
     * [ViewPager2.SCROLL_STATE_IDLE],
     * [ViewPager2.SCROLL_STATE_DRAGGING],
     * [ViewPager2.SCROLL_STATE_SETTLING].
     */
    fun onPageScrollStateChanged(@ViewPager2.ScrollState state: Int)
}
