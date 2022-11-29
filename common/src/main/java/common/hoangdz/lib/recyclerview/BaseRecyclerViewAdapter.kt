package common.hoangdz.lib.recyclerview

import android.content.Context
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import common.hoangdz.lib.extensions.findIndex
import common.hoangdz.lib.extensions.launchMain
import common.hoangdz.lib.extensions.unRegisterEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import java.util.*

abstract class BaseRecyclerViewAdapter<D>(protected val context: Context) :
    RecyclerView.Adapter<BaseViewHolder<*, D>>() {

    var onItemClick: ((D, Int) -> Unit)? = null


    private val onDataChanges by lazy { mutableListOf<(MutableList<D>, justModified: Boolean) -> Unit>() }

    fun addOnDataChange(onDataChange: (MutableList<D>, justModified: Boolean) -> Unit) {
        this.onDataChanges.add(onDataChange)
    }

    var onRequestMovingItem: ((BaseViewHolder<*, D>) -> Unit)? = null


    protected var adapterScope: CoroutineScope? = null

    private var hasDataBefore = false

    operator fun get(position: Int) = dataList[position]

    val dataList = mutableListOf<D>()

    val indices
        get() = dataList.indices

    protected var recyclerView: RecyclerView? = null

    protected open val animateAlphaWhenReplaceData = true

    protected open val needSubscribeEventBus = false

    private var replaceDataJob: Job? = null

    fun findItem(onFind: (D) -> Boolean): D? {
        for (d in dataList) {
            if (onFind(d))
                return d
        }
        return null
    }

    fun findIndex(onFind: (D) -> Boolean): Int {
        for ((index, d) in dataList.withIndex()) {
            if (onFind(d))
                return index
        }
        return -1
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapterScope = CoroutineScope(Dispatchers.IO)
        this.recyclerView = recyclerView
        if (needSubscribeEventBus && !EventBus.getDefault()
                .isRegistered(this)
        ) EventBus.getDefault()
            .register(this)

        if (hasDataBefore)
            onDataChanges.forEach { it.invoke(dataList, true) }

    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapterScope?.cancel()
        this.recyclerView = null
        unRegisterEventBus()
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onEventBus(eventData: EventBusData) {
//    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, D>, position: Int) {
        val pos = holder.adapterPosition
        val data = dataList[pos]
        holder.bindView(data, holder.adapterPosition)
        holder.itemClickListener = { d, p ->
            onItemClick?.invoke(d, p)
        }
    }

    override fun getItemCount() = dataList.size

    open fun replaceData(data: MutableList<D>) {
        hasDataBefore = true
        fun replace() {
            this.dataList.clear()
            this.dataList.addAll(data)
            notifyDataSetChanged()
            onDataChanges.forEach {
                it.invoke(data, false)
            }
        }

        val hasDataPrevious = dataList.isNotEmpty()
        if (!hasDataPrevious || !animateAlphaWhenReplaceData)
            replace()
        else {
            replaceDataJob?.cancel()
            replaceDataJob = adapterScope?.launchMain {
//                recyclerView?.alphaAnimate(
//                    0f,
//                    MyCustomRecyclerView.DEFAULT_ANIMATED_DURATION / 2,
//                    false
//                )?.awaitAnimation()
                replace()
            }
        }

    }

    open fun removeAt(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size - position)
        onDataChanges.forEach {
            it.invoke(dataList, true)
        }
    }

    open fun remove(data: D) {
        val index = findIndex { data == it }.takeIf { it > -1 } ?: return
        dataList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, dataList.size - index)
        onDataChanges.forEach {
            it.invoke(dataList, true)
        }
    }

    open fun addData(data: D) {
        hasDataBefore = true
        dataList.add(data)
        notifyItemInserted(dataList.lastIndex)
        onDataChanges.forEach {
            it.invoke(dataList, true)
        }
    }

    open fun addData(position: Int, data: D) {
        hasDataBefore = true
        dataList.add(position, data)
        notifyItemInserted(0)
        notifyItemRangeChanged(1, dataList.size - 1)
        onDataChanges.forEach {
            it.invoke(dataList, true)
        }
    }

    fun updateItem(data: D) {
        val index = dataList.findIndex { it == data }.takeIf { it > -1 } ?: return
        notifyItemChanged(index)
    }

    open fun setItem(position: Int, data: D) {
        if (position !in dataList.indices) return
        dataList[position] = data
        notifyItemChanged(position)
    }

    open fun moveItem(from: Int, to: Int) {
        Collections.swap(dataList, from, to)
        this.notifyItemMoved(from, to)
    }
}