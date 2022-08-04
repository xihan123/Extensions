package cn.xihan.lib.banner.config

import cn.xihan.lib.banner.util.BannerUtils

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:01
 * @介绍 :
 */
object BannerConfig {
   const val IS_AUTO_LOOP = true
   const val IS_INFINITE_LOOP = true
   const val LOOP_TIME = 3000
   const val SCROLL_TIME = 600
   const val INCREASE_COUNT = 2
   const val INDICATOR_NORMAL_COLOR = -0x77000001
   const val INDICATOR_SELECTED_COLOR = -0x78000000
   val INDICATOR_NORMAL_WIDTH: Int = BannerUtils.dp2px(5.toFloat())
   val INDICATOR_SELECTED_WIDTH: Int = BannerUtils.dp2px(7.toFloat())
   val INDICATOR_SPACE: Int = BannerUtils.dp2px(5.toFloat())
   val INDICATOR_MARGIN: Int = BannerUtils.dp2px(5.toFloat())
   val INDICATOR_HEIGHT: Int = BannerUtils.dp2px(3.toFloat())
   val INDICATOR_RADIUS: Int = BannerUtils.dp2px(3.toFloat())
}
