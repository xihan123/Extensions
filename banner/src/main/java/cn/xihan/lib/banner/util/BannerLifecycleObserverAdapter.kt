package cn.xihan.lib.banner.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class BannerLifecycleObserverAdapter(
    private val mLifecycleOwner: LifecycleOwner,
    private val mObserver: BannerLifecycleObserver
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        LogUtils.i("onStart")
        mObserver.onStart(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        LogUtils.i("onStop")
        mObserver.onStop(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtils.i("onDestroy")
        mObserver.onDestroy(mLifecycleOwner)
    }
}