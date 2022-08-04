package cn.xihan.lib.pufferdb.android

import android.content.Context
import cn.xihan.lib.pufferdb.core.Puffer
import cn.xihan.lib.pufferdb.core.PufferDB
import cn.xihan.lib.pufferdb.core.PufferException
import java.io.File

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 19:50
 * @介绍 :
 */
object AndroidPufferDB {
    private const val DEFAULT_FILE_NAME = "puffer.db"

    private lateinit var puffer: Puffer
    private lateinit var filesDir: File

    fun init(context: Context) {
        filesDir = context.filesDir
        puffer = PufferDB.with(getInternalFile(DEFAULT_FILE_NAME))
    }

    fun withDefault() =
        if (::puffer.isInitialized) {
            puffer
        } else {
            throw PufferException("${this::class.java.simpleName} was not initialized")
        }

    fun getInternalFile(fileName: String) =
        if (::filesDir.isInitialized) {
            File(filesDir, fileName)
        } else {
            throw PufferException("${this::class.java.simpleName} was not initialized")
        }
}

