package common.hoangdz.lib.widget.my_pager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import common.hoangdz.lib.components.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import common.hoangdz.lib.widget.BottomMenuNavigation


/**
 * Created by Hoang Dep Trai on 14/04/2022.
 */

class PagerView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val onPageSelectedCalls by lazy { mutableListOf<((Int) -> Unit)>() }

    private val containers by lazy { mutableListOf<FrameLayout>() }

    private val fragmentList by lazy { mutableListOf<BaseFragment<*>>() }

    private var pagerManager: PagerManager? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun setupWithBottomNavigation(
        bottomNav: BottomMenuNavigation
    ) {
        bottomNav.onMenuItemSelected = { p ->
            openPage(p)
        }
        post {
            openPage(bottomNav.selectedItem)
        }
    }

    private fun onPageSelect(p: Int) {
        onPageSelectedCalls.forEach { it.invoke(p) }
    }

    fun openPage(pageIndex: Int) {
        pagerManager?.openPage(pageIndex)
        onPageSelect(pageIndex)
    }

    fun addOnPageSelectedListener(call: (Int) -> Unit) {
        onPageSelectedCalls.add(call)
    }

    fun addFragments(
        fragments: MutableList<BaseFragment<*>>,
        fm: FragmentManager
    ) {
        removeAllViews()
        this.fragmentList.clear()
        this.fragmentList.addAll(fragments)
        for (fragment in fragments) {
            containers.add(createContainer())
        }
        pagerManager = PagerManager(containers, fm, fragmentList, scope)
    }

    fun setupPagerWithBottomNav(
        bottomNav: BottomMenuNavigation,
        fragments: MutableList<BaseFragment<*>>,
        fm: FragmentManager
    ) {
        addFragments(fragments, fm)
        setupWithBottomNavigation(bottomNav)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAllViews()
        scope.cancel()
    }

    private fun createContainer(): FrameLayout {
        return FrameLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            visibility = GONE
        }.apply {
            id = ViewCompat.generateViewId()
            this@PagerView.addView(this)
        }
    }

}