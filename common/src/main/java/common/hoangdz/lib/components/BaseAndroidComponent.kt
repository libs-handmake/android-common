package common.hoangdz.lib.components

import android.os.Bundle
import androidx.viewbinding.ViewBinding


/**
 * Created by Hoang Dep Trai on 05/23/2022.
 */

interface BaseAndroidComponent<VB : ViewBinding> {

    val binding:VB

    val needToSubscribeEventBus: Boolean

    fun init(savedInstanceState: Bundle?){
        binding.initView(savedInstanceState)
        binding.setupViewModel()
        binding.initListener()
    }

    fun VB.initView(savedInstanceState: Bundle?)

    fun VB.initListener()

    fun VB.setupViewModel()

    fun onBackPress() = false

    fun updateContentHeightAds(height: Int) {
    }

}