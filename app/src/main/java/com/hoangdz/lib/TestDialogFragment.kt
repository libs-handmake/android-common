package com.hoangdz.lib

import com.hoangdz.lib.databinding.DialogTestBinding
import common.hoangdz.lib.components.BaseDialogFragment

/**
 * Created by HoangDepTrai on 29, November, 2022 at 10:22 AM
 */
class TestDialogFragment : BaseDialogFragment<DialogTestBinding>() {
    companion object{
        fun newInstance() = TestDialogFragment()
    }
}