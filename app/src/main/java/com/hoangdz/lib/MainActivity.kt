package com.hoangdz.lib

import android.os.Bundle
import com.hoangdz.lib.databinding.ActivityMainBinding
import common.hoangdz.lib.components.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun ActivityMainBinding.initView(savedInstanceState: Bundle?) {
        TestDialogFragment.newInstance().show(supportFragmentManager)
    }
}