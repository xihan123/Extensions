package cn.xihan.lib.banner.adapter



import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.xihan.lib.banner.R
import cn.xihan.lib.banner.config.BannerConfig
import cn.xihan.lib.banner.holder.IViewHolder
import cn.xihan.lib.banner.listener.OnBannerListener
import cn.xihan.lib.banner.util.BannerUtils.getRealPosition


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 14:56
 * @介绍 :
 */
abstract class BannerAdapter<T, VH : RecyclerView.ViewHolder>(var mDatas: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<VH>(),
    IViewHolder<T, VH> {

    private var mOnBannerListener: OnBannerListener<T>? = null
    private var mViewHolder: VH? = null
    private var mIncreaseCount: Int = BannerConfig.INCREASE_COUNT


    override fun onBindViewHolder(holder: VH, position: Int) {
        mViewHolder = holder
        val real = getRealPosition(position)
        val data = mDatas[real]
        holder.itemView.setTag(R.id.banner_data_key, data)
        holder.itemView.setTag(R.id.banner_pos_key, real)
        onBindView(holder, mDatas[real], real, getRealCount())
        if (mOnBannerListener != null) {
            holder.itemView.setOnClickListener {
                mOnBannerListener?.OnBannerClick(data, real)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = onCreateHolder(parent, viewType)
        vh.itemView.setOnClickListener {
            if (mOnBannerListener != null) {
                val data = vh.itemView.getTag(R.id.banner_data_key) as T
                val real = vh.itemView.getTag(R.id.banner_pos_key) as Int
                mOnBannerListener?.OnBannerClick(data, real)
            }
        }
        return vh
    }

    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param datas
     */
    fun setDatas(datas: MutableList<T>) {
        mDatas.clear()
        mDatas = datas
        notifyDataSetChanged()
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 真实的position
     * @return
     */
    open fun getData(position: Int): T {
        return mDatas[position]
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 这里传的position不是真实的，获取时转换了一次
     * @return
     */
    open fun getRealData(position: Int): T {
        return mDatas[getRealPosition(position)]
    }

    override fun getItemCount(): Int {
        return if (getRealCount() > 1) getRealCount() + mIncreaseCount else getRealCount()
    }

    open fun getRealCount(): Int {
        return mDatas.size
    }

    open fun getRealPosition(position: Int): Int {
        return getRealPosition(
            mIncreaseCount == BannerConfig.INCREASE_COUNT,
            position,
            getRealCount()
        )
    }

    open fun setOnBannerListener(listener: OnBannerListener<T>) {
        mOnBannerListener = listener
    }

    open fun getViewHolder(): VH {
        return mViewHolder!!
    }

    open fun setIncreaseCount(increaseCount: Int) {
        mIncreaseCount = increaseCount
    }

}