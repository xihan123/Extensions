package cn.xihan.lib.pufferdb.initializer

import android.content.Context
import androidx.startup.Initializer
import cn.xihan.lib.pufferdb.android.AndroidPufferDB

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 19:55
 * @介绍 :
 */
class PufferdbInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AndroidPufferDB.init(context)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}