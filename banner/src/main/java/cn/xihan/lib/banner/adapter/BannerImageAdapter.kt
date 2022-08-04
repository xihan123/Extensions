package cn.xihan.lib.banner.adapter

import android.view.ViewGroup
import android.widget.ImageView
import cn.xihan.lib.banner.holder.BannerImageHolder


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:18
 * @介绍 :
 */
abstract class BannerImageAdapter<T>(mData: MutableList<T> = mutableListOf()) :
    BannerAdapter<T, BannerImageHolder>(mData) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerImageHolder {
        val imageView = ImageView(parent?.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerImageHolder(imageView)
    }
}
