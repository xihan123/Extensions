package cn.xihan.lib.banner.transformer

import android.view.View
import kotlin.math.abs
import kotlin.math.max

class ZoomOutPageTransformer(minScale: Float, minAlpha: Float) : BasePageTransformer() {
    private var mMinScale = minScale
    private var mMinAlpha = minAlpha


    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = max(mMinScale, 1 - abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.translationX = horzMargin - vertMargin / 2
            } else {
                view.translationX = -horzMargin + vertMargin / 2
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor

            // Fade the page relative to its size.
            view.alpha = mMinAlpha +
                    (scaleFactor - mMinScale) /
                    (1 - mMinScale) * (1 - mMinAlpha)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }

    companion object {
        private const val DEFAULT_MIN_SCALE = 0.85f
        private const val DEFAULT_MIN_ALPHA = 0.5f
    }
}