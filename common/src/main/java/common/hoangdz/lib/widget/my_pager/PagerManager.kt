package common.hoangdz.lib.widget.my_pager

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import common.hoangdz.lib.components.BaseFragment
import common.hoangdz.lib.extensions.findFragment
import common.hoangdz.lib.extensions.remove
import common.hoangdz.lib.extensions.replace
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Hoang Dep Trai on 14/04/2022.
 */

class PagerManager(
    private val containers: MutableList<FrameLayout>,
    private val fm: FragmentManager,
    private val fragmentList: MutableList<BaseFragment<*>>,
    private val scope: CoroutineScope
) {

    companion object {
        private const val FIRST_OPEN_GAP = 400L
    }

    private val pageMap by lazy { hashMapOf<Fragment, Boolean>() }

    private var currentPosition = 0

    fun openPage(position: Int) {
        if (position in fragmentList.indices) {
            containers[currentPosition].visibility = View.GONE
            if (pageMap[fragmentList[position]] == null) {
                (fm.findFragment(fragmentList[position])
                    ?.apply {
                        if (this is BaseFragment<*>) {
                            fragmentList[position] = this
                            val parent = this.view?.parent
                            if (parent != null) {
                                parent as FrameLayout
                                parent.removeAllViews()
                                containers[position].addView(this.view)
                            } else
                                containers[position].addView(this.view ?: kotlin.run {
                                    fm.remove(this)
                                    openPage(position)
                                    return
                                })
                        }
                    } ?: fragmentList[position].also {
                    fm.replace(containers[position], it)
                }).also { pageMap[it] = true }
            }
            containers[position].visibility = View.VISIBLE
            currentPosition = position
        }
    }

}