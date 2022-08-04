package cn.xihan.lib.mmkv

import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 18:15
 * @介绍 :
 */
class MMKVInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        MMKV.initialize(context)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}
