package cn.xihan.extensionstest

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.RecyclerView
import cn.xihan.extensionstest.databinding.ActivityBannerBinding
import cn.xihan.lib.banner.adapter.BannerAdapter
import cn.xihan.lib.banner.indicator.CircleIndicator


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 16:46
 * @介绍 :
 */
class BannerActivity: AppCompatActivity() {

    private val binding by lazy { ActivityBannerBinding.inflate(layoutInflater) }

    val item = mutableListOf(DataEntity("1",R.mipmap.ic_launcher),DataEntity("2",R.mipmap.ic_launcher))

    val adapter by lazy { ImageAdapter(item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.banner.apply {
            addBannerLifecycleObserver(this@BannerActivity)
            setAdapter(adapter)
            setIndicator(CircleIndicator(this@BannerActivity))
        }




    }

}

data class DataEntity(
    val title: String,
    @DrawableRes
    val cover: Int
)

class ImageAdapter(mDatas: MutableList<DataEntity>): BannerAdapter<DataEntity, ImageAdapter.BannerViewHolder>(mDatas) {


    class BannerViewHolder(@param:NonNull var imageView: ImageView) :
        RecyclerView.ViewHolder(
            imageView
        )

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView);
    }

    override fun onBindView(holder: BannerViewHolder, data: DataEntity, position: Int, size: Int) {
        holder.imageView.setImageResource(data.cover)
    }
}