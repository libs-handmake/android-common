package common.hoangdz.lib.components

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import common.hoangdz.lib.extensions.inflateViewBinding
import common.hoangdz.lib.extensions.registerEventBusBy
import common.hoangdz.lib.extensions.remove
import common.hoangdz.lib.extensions.unRegisterEventBus


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseAndroidComponent<VB> {

    override val binding by lazy { inflateViewBinding<VB>(layoutInflater) }

    override val needToSubscribeEventBus = false

    open var onPermissionResult: ((Int, Array<out String>, IntArray) -> Unit)? = null

    open var onActivityResult: ((Intent?) -> Unit)? = null

    override fun onStart() {
        super.onStart()
        registerEventBusBy(needToSubscribeEventBus)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        savedInstanceState?.let { removeAllDialogFragment() }
        init(savedInstanceState)
    }

    override fun VB.setupViewModel() {}

    override fun VB.initListener() {}

    override fun VB.initView(savedInstanceState: Bundle?) {}

    @CallSuper
    override fun onBackPressed() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment<*>) {
                if (fragment.requestBackPressed()) {
                    return
                }
            }
        }
        if (onBackPress()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterEventBus()
    }

    private fun removeAllDialogFragment() {
        supportFragmentManager.apply {
            for (fragment in fragments) {
                if (fragment is DialogFragment) {
                    if (fragment.isAdded) {
                        fragment.dismissAllowingStateLoss()
                    }
                    remove(fragment)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onPermissionResult?.invoke(requestCode, permissions, grantResults)
    }

    val activityLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            onActivityResult?.invoke(data)
        }
    }
}