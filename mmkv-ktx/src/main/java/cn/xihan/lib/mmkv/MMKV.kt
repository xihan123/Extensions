package cn.xihan.lib.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/4 18:16
 * @介绍 :
 */
/**
 * A class that has a MMKV object. If you want to customize the MMKV,
 * you can override the kv object. For example:
 *
 * ```kotlin
 * object DataRepository : MMKVOwner {
 *
 *   override val kv: MMKV = MMKV.mmkvWithID("MyID")
 * }
 * ```
 *
 * @author Dylan Cai
 */
interface MMKVOwner {
    val kv: MMKV get() = cn.xihan.lib.mmkv.kv
}

val kv: MMKV = MMKV.defaultMMKV()

fun MMKVOwner.mmkvInt(default: Int = 0) =
    MMKVProperty(MMKV::decodeInt, MMKV::encode, default)

fun MMKVOwner.mmkvLong(default: Long = 0L) =
    MMKVProperty(MMKV::decodeLong, MMKV::encode, default)

fun MMKVOwner.mmkvBool(default: Boolean = false) =
    MMKVProperty(MMKV::decodeBool, MMKV::encode, default)

fun MMKVOwner.mmkvFloat(default: Float = 0f) =
    MMKVProperty(MMKV::decodeFloat, MMKV::encode, default)

fun MMKVOwner.mmkvDouble(default: Double = 0.0) =
    MMKVProperty(MMKV::decodeDouble, MMKV::encode, default)

fun MMKVOwner.mmkvString() =
    MMKVProperty(MMKV::decodeString, MMKV::encode, null)

fun MMKVOwner.mmkvString(default: String) =
    MMKVPropertyWithDefault(MMKV::decodeString, MMKV::encode, default)

fun MMKVOwner.mmkvStringSet(): ReadWriteProperty<MMKVOwner, Set<String>?> =
    MMKVProperty(MMKV::decodeStringSet, MMKV::encode, null)

fun MMKVOwner.mmkvStringSet(default: Set<String>) =
    MMKVPropertyWithDefault(MMKV::decodeStringSet, MMKV::encode, default)

fun MMKVOwner.mmkvBytes() =
    MMKVProperty(MMKV::decodeBytes, MMKV::encode, null)

fun MMKVOwner.mmkvBytes(default: ByteArray) =
    MMKVPropertyWithDefault(MMKV::decodeBytes, MMKV::encode, default)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable() =
    MMKVParcelableProperty(T::class.java)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable(default: T) =
    MMKVParcelablePropertyWithDefault(T::class.java, default)

class MMKVProperty<V>(
    private val decode: MMKV.(String, V) -> V,
    private val encode: MMKV.(String, V) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decode(property.name, defaultValue)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVPropertyWithDefault<V>(
    private val decode: MMKV.(String, V?) -> V?,
    private val encode: MMKV.(String, V?) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decode(property.name, null) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVParcelableProperty<V : Parcelable>(
    private val clazz: Class<V>
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decodeParcelable(property.name, clazz)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVParcelablePropertyWithDefault<V : Parcelable>(
    private val clazz: Class<V>,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decodeParcelable(property.name, clazz) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}
