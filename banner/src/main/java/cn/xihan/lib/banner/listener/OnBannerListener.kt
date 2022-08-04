package cn.xihan.lib.banner.listener

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:06
 * @介绍 :
 */
interface OnBannerListener<T> {

    /**
     * 点击事件
     *
     * @param data     数据实体
     * @param position 当前位置
     */
    fun OnBannerClick(data: T, position: Int)

}