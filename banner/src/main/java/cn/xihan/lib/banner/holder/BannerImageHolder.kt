package cn.xihan.lib.banner.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 14:58
 * @介绍 :
 */
class BannerImageHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView

    init {
        imageView = view as ImageView
    }
}