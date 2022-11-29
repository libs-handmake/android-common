package common.hoangdz.lib.extensions

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus

/**
 * Created by Hoang Dep Trai on 07/01/2022.
 */

fun <T> List<T>.findIndex(find: (T) -> Boolean): Int {
    for ((index, item) in withIndex()) {
        if (find(item)) {
            return index
        }
    }
    return -1
}

fun <T> Sequence<T>.findIndex(find: (T) -> Boolean): Int {
    for ((index, item) in withIndex()) {
        if (find(item)) {
            return index
        }
    }
    return -1
}

inline fun <T> List<T>.findItemAndIndex(condition: (T) -> Boolean, onFound: (Int, T) -> Unit) {
    for ((index, item) in withIndex()) {
        if (condition(item)) {
            onFound(index, item)
            return
        }
    }
}

fun <T> List<T>.containBy(condition: (T) -> Boolean): Boolean {
    for (item in this) {
        if (condition(item)) {
            return true
        }
    }
    return false
}

inline fun <reified T> String.createFromJson(): T? {
    val result = try {
        Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)
    } catch (e: JsonSyntaxException) {
        null
    }
    return result
}

inline fun <reified T : Any> T.clone() = this.toJson().createFromJson<T>()

fun Any.registerEventBusBy(needToSubscribe: Boolean = true) {
    if (!EventBus.getDefault().isRegistered(this) && needToSubscribe) {
        EventBus.getDefault().register(this)
    }
}

fun Any.unRegisterEventBus() {
    if (EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().unregister(this)
    }
}

fun <T> MutableList<T>.removeBy(condition: (T) -> Boolean): Boolean {
    val value = find { condition(it) } ?: return false
    return remove(value)
}

fun <T> List<T>.equal(another: List<T>, onEqual: (T, T) -> Boolean): Boolean {
    if (this.size != another.size) return false
    for (item in this) {
        if (!another.containBy { onEqual(it, item) }) {
            return false
        }
    }
    return true
}