package common.hoangdz.lib.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB : ViewBinding, D>(val vb: VB) :
    RecyclerView.ViewHolder(vb.root) {
    var itemClickListener: ((D, Int) -> Unit)? = null

    private var isFocus = false

    fun focus() {
        isFocus = true
    }

    fun unfocus() {
        isFocus = false
    }

    fun bindView(data: D, position: Int) {
        vb.bindView(data, position)
        if (isFocus) {
            vb.root.apply {
                scaleX = 1.5f
                scaleY = 1.5f
            }
        } else
            vb.root.apply {
                scaleX = 1f
                scaleY = 1f
            }
    }

    protected abstract fun VB.bindView(data: D, position: Int)
}