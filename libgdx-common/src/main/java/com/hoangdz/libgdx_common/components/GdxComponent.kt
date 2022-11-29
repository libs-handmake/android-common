package com.hoangdz.libgdx_common.components

import android.os.Bundle
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/**
 * Created by HoangDepTrai on 23, November, 2022 at 9:00 AM
 */
interface GdxComponent {

    val gdxApp: ApplicationListener

    fun initView(state: Bundle?)

    fun setupViewModel()

    fun initListener()

    fun getConfiguration(): AndroidApplicationConfiguration {
        val config = AndroidApplicationConfiguration()
        config.a = 8
        config.b = config.a
        config.g = config.b
        config.r = config.g
        config.useGyroscope = true
        config.useAccelerometer = false
        config.useCompass = false
        return config
    }

    fun init(state: Bundle?) {
        initView(state)
        setupViewModel()
        initListener()
    }

}