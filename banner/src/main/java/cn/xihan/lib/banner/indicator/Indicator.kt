package cn.xihan.lib.banner.indicator

import android.view.View
import cn.xihan.lib.banner.config.IndicatorConfig
import cn.xihan.lib.banner.listener.OnPageChangeListener


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:20
 * @介绍 :
 */
interface Indicator : OnPageChangeListener {

    fun getIndicatorView(): View?

    fun getIndicatorConfig(): IndicatorConfig

    fun onPageChanged(count: Int, currentPosition: Int)
}