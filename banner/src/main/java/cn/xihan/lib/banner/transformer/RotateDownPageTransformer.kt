package cn.xihan.lib.banner.transformer

import android.view.View

class RotateDownPageTransformer(maxRotate: Float) : BasePageTransformer() {
    private var mMaxRotate = maxRotate

    override fun transformPage(view: View, position: Float) {
        if (position < -1) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.rotation = mMaxRotate * -1
            view.pivotX = view.width.toFloat()
            view.pivotY = view.height.toFloat()
        } else if (position <= 1) { // [-1,1]
            if (position < 0) { //[0，-1]
                view.pivotX =
                    view.width * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
                view.pivotY = view.height.toFloat()
                view.rotation = mMaxRotate * position
            } else { //[1,0]
                view.pivotX =
                    view.width * DEFAULT_CENTER * (1 - position)
                view.pivotY = view.height.toFloat()
                view.rotation = mMaxRotate * position
            }
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            view.rotation = mMaxRotate
            view.pivotX = 0f
            view.pivotY = view.height.toFloat()
        }
    }

    companion object {
        private const val DEFAULT_MAX_ROTATE = 15.0f
    }
}