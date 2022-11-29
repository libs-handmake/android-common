package common.hoangdz.lib.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * Created by Hoang Dep Trai on 05/30/2022.
 */

abstract class BaseViewPagerAdapter(
    private val fragmentList: MutableList<Fragment>,
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    init {
        restoreFragmentIfExist()
    }


    private fun restoreFragmentIfExist() {
        for ((index, fragment) in fragmentList.withIndex()) {
            fragmentList[index] =
                fragmentManager.findFragmentByTag("f${getItemId(index)}") ?: fragment
        }
    }

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

}