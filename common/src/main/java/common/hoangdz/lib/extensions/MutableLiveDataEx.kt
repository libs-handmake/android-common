package common.hoangdz.lib.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

fun <T> MutableLiveData<MutableList<T>>.addNewItems(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(item)
    this.postValue(oldValue)
}

fun <T> MutableLiveData<MutableList<T>>.addNewItemAt(index: Int, item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(index, item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.postAddNewItemAt(index: Int, item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(index, item)
    this.postValue(oldValue)
}

fun <T> MutableLiveData<MutableList<T>>.removeItem(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.remove(item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.postRemoveItem(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.remove(item)
    this.postValue(oldValue)
}

fun <T> MutableLiveData<MutableList<T>>.updateItem(item: T, position: Int) {
    val oldValue = this.value ?: mutableListOf()
    oldValue[position] = item
    this.value = oldValue
}


fun <T> MutableLiveData<MutableList<T>>.postUpdateItem(item: T, position: Int) {
    val oldValue = this.value ?: mutableListOf()
    oldValue[position] = item
    this.postValue(oldValue)
}


fun <T> MutableLiveData<MutableList<T>>.addNewItems(items: List<T>) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.addAll(items)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.unSelectedAll() {
    this.postValue(mutableListOf())
}

fun <T> MutableLiveData<MutableList<T>>.swapItems(fromPosition: Int, toPosition: Int) {
    val oldValue = this.value ?: mutableListOf()
    if (fromPosition < toPosition) {
        for (i in fromPosition until toPosition) {
            Collections.swap(oldValue, i, i + 1)
        }
    } else {
        for (i in fromPosition downTo toPosition + 1) {
            Collections.swap(oldValue, i, i - 1)
        }
    }
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.postSwapItems(fromPosition: Int, toPosition: Int) {
    val oldValue = this.value ?: mutableListOf()
    if (fromPosition < toPosition) {
        for (i in fromPosition until toPosition) {
            Collections.swap(oldValue, i, i + 1)
        }
    } else {
        for (i in fromPosition downTo toPosition + 1) {
            Collections.swap(oldValue, i, i - 1)
        }
    }
    this.postValue(oldValue)
}

fun <T> MutableLiveData<T>.mutation(actions: (MutableLiveData<T>) -> Unit) {
    actions(this)
    this.value = this.value
}

fun <T> MutableLiveData<T>.postMutation(actions: (MutableLiveData<T>) -> Unit) {
    actions(this)
    this.postValue(this.value)
}

fun <T> merge(first: List<T>, second: List<T>): List<T> {
    return first.plus(second)
}

fun <T> LiveData<T>.observeSingle(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner) {
        observer(it)
        removeObservers(owner)
    }
}