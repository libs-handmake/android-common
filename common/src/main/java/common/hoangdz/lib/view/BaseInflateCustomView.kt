package common.hoangdz.lib.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import common.hoangdz.lib.extensions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseInflateCustomView<VB : ViewBinding> : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    protected val binding by lazy {
        inflateViewBinding<VB>(context.layoutInflater, this, true)
    }

    protected var viewScope: CoroutineScope? = null

    protected open val needToSubscribeEventBus = false


    init {
        doOnViewDrawn {
            binding.onViewDrawn()
        }
    }


    protected open fun VB.onViewDrawn() {}

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewScope = CoroutineScope(Dispatchers.IO)
        registerEventBusBy(needToSubscribeEventBus)
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unRegisterEventBus()
        viewScope?.cancel()
    }
}