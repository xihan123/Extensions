package cn.xihan.lib.banner.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 15:04
 * @介绍 :
 */
class MarginDecoration(@Px margin: Int) : RecyclerView.ItemDecoration() {

    private val mMarginPx: Int

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val linearLayoutManager: LinearLayoutManager = requireLinearLayoutManager(parent)
        if (linearLayoutManager.orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mMarginPx
            outRect.bottom = mMarginPx
        } else {
            outRect.left = mMarginPx
            outRect.right = mMarginPx
        }
    }

    private fun requireLinearLayoutManager(@NonNull parent: RecyclerView): LinearLayoutManager {
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            return layoutManager
        }
        throw IllegalStateException("The layoutManager must be LinearLayoutManager")
    }

    init {
        mMarginPx = margin
    }
}
