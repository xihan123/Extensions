package cn.xihan.lib.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 18:17
 * @介绍 :
 */
class SpUtil(name: String = "") {

    var mmkv: MMKV? = null

    init {
        mmkv = if (name.isEmpty()) {
            MMKV.defaultMMKV()
        } else {
            MMKV.mmkvWithID(name)
        }
    }

    fun encode(key: String, value: Any?) {
        try {
            when (value) {
                is String -> mmkv?.encode(key, value)
                is Float -> mmkv?.encode(key, value)
                is Boolean -> mmkv?.encode(key, value)
                is Int -> mmkv?.encode(key, value)
                is Long -> mmkv?.encode(key, value)
                is Double -> mmkv?.encode(key, value)
                is ByteArray -> mmkv?.encode(key, value)
                is Parcelable -> mmkv?.encode(key, value)
                else -> return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun encode(key: String, sets: Set<String>?) {
        sets?.let { mmkv?.encode(key, sets) }
    }

    inline fun <reified T> encode(key: String, sets: List<T>) {
        sets.isNotEmpty().let {
            try {
                val strJson = kJson.encodeToString(sets)
                encode(key, strJson)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inline fun <reified T> decodeList(key: String): MutableList<T> {
        val list = mutableListOf<T>()
        try {
            val strJson = decodeString(key)
            if (strJson.isNotBlank()) {
                list.addAll(kJson.decodeFromString(strJson))
                return list
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun decodeInt(key: String, default: Int = 0): Int {
        return mmkv?.decodeInt(key, default) ?: default
    }

    fun decodeDouble(key: String, default: Double = 0.00): Double {
        return mmkv?.decodeDouble(key, default) ?: default
    }

    fun decodeLong(key: String, default: Long = 0L): Long {
        return mmkv?.decodeLong(key, default) ?: default
    }

    fun decodeBoolean(key: String, default: Boolean = false): Boolean {
        return mmkv?.decodeBool(key, default) ?: default
    }

    fun decodeFloat(key: String, default: Float = 0F): Float {
        return mmkv?.decodeFloat(key, default) ?: default
    }

    fun decodeByteArray(key: String, default: ByteArray = ByteArray(0)): ByteArray {
        return mmkv?.decodeBytes(key) ?: default
    }

    fun decodeString(key: String, default: String = ""): String {
        return mmkv?.decodeString(key, default) ?: default
    }

    inline fun <reified T : Parcelable> decodeParcelable(key: String, tClass: Class<T>): T? {
        return mmkv?.decodeParcelable(key, tClass)// ?: T::class.java.newInstance()
    }

    inline fun <reified T : Parcelable> decodeParcelableNotNull(key: String, tClass: Class<T>): T {
        return mmkv?.decodeParcelable(key, tClass) ?: T::class.java.newInstance()
    }

    fun decodeStringSet(key: String, default: Set<String> = Collections.emptySet()): Set<String> {
        return mmkv?.decodeStringSet(key, Collections.emptySet()) ?: default
    }

    fun removeKey(key: String) {
        mmkv?.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv?.clearAll()
    }

}

val kJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson(): String = kJson.encodeToString(this)

/**
 * 将JSON字符串转为对象
 */
inline fun <reified T> String.toObject(): T = kJson.decodeFromString(this)

// 批处理
fun SpUtil.encodes(action: SpUtil.() -> Unit) {
    action()
}




