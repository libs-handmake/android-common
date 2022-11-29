package com.hoangdz.libgdx_common.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

/**
 * Created by HoangDepTrai on 23, November, 2022 at 8:47 AM
 */
abstract class GdxFragment : AndroidFragmentApplication(), GdxComponent {

    override fun initView(state: Bundle?) {
    }

    override fun setupViewModel() {
    }

    override fun initListener() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initializeForView(gdxApp, getConfiguration())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

}