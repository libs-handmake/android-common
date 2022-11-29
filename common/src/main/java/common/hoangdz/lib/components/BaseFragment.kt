package common.hoangdz.lib.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import common.hoangdz.lib.extensions.inflateViewBinding
import common.hoangdz.lib.extensions.registerEventBusBy
import common.hoangdz.lib.extensions.unRegisterEventBus

abstract class BaseFragment<VB : ViewBinding> : Fragment(), BaseAndroidComponent<VB> {

    override val needToSubscribeEventBus = false

    override val binding: VB by lazy { inflateViewBinding(layoutInflater) }

    override fun onStart() {
        super.onStart()
        registerEventBusBy(needToSubscribeEventBus)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    fun requestBackPressed(): Boolean {
        var i = -1
        while (++i < childFragmentManager.fragments.size) {
            childFragmentManager.fragments[i].let {
                if (it is BaseFragment<*>) {
                    if (it.requestBackPressed())
                        return true
                }
            }
        }
        return onBackPress()
    }

    override fun VB.initListener() {}

    override fun VB.setupViewModel() {}

    override fun VB.initView(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        unRegisterEventBus()
    }

}
