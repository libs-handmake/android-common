package common.hoangdz.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import common.hoangdz.lib.R
import common.hoangdz.lib.databinding.LayoutItemBottomMenuBinding
import common.hoangdz.lib.extensions.*


/**
 * Created by Hoang Dep Trai on 04/26/2022.
 */

class BottomMenuNavigation : LinearLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typeArr = context?.obtainStyledAttributes(attrs, R.styleable.BottomMenuNavigation)
        try {
            colorSelected =
                typeArr?.getColor(R.styleable.BottomMenuNavigation_color_selected, colorSelected)
                    ?: colorSelected
            colorUnSelected =
                typeArr?.getColor(
                    R.styleable.BottomMenuNavigation_color_unselected,
                    colorUnSelected
                )
                    ?: colorUnSelected
        } finally {
            typeArr?.recycle()
        }
    }

    companion object {
        private const val ANIMATE_SELECTED_ITEM_DURATION = 200L
    }

    private val menuItems by lazy { mutableListOf<MenuItem>() }

    private val itemsMenuBinding by lazy { mutableListOf<LayoutItemBottomMenuBinding>() }

    private val paddingSize by lazy { 4.dpInt }

    var onMenuItemSelected: ((Int) -> Unit)? = null

    var selectedItem = 0

    private var colorSelected = context.getColorR(R.color.black)

    private var colorUnSelected = context.getColorR(R.color.gray)


    init {
        orientation = HORIZONTAL
        setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
    }

    fun addMenuItems(vararg menuItems: MenuItem) {
        this.menuItems.addAll(menuItems)
        for ((index, item) in menuItems.withIndex()) {
            createMenuItem(index, item)
        }
    }

    fun getItemAt(p: Int) = if (p in menuItems.indices) this.menuItems[p] else null

    fun setItemSelected(position: Int) {
        selectedItemAt(position)
    }

    private fun createMenuItem(index: Int, menuItem: MenuItem) {
        val itemBinding = LayoutItemBottomMenuBinding.inflate(context.layoutInflater).apply {
            tvMenuTitle.text = menuItem.title
            menuItem.animation?.let { lottieMenuView.setAnimation(it) }
                ?: kotlin.run { lottieMenuView.gone() }
            menuItem.drawable?.let { imgMenuItem.setImageResource(it) }
                ?: kotlin.run { imgMenuItem.gone() }
            changeSateItemView(menuItem)
            root.clickNoAnim {
                selectedItemAt(index)
            }
            itemsMenuBinding.add(this)
        }
        addView(itemBinding.root.apply { layoutParams = LayoutParams(0, WRAP_CONTENT, 1f) })
    }

    private fun selectedItemAt(index: Int) {
        if (index == selectedItem) return
        val previousItemSelected = selectedItem
        selectedItem = index
        itemsMenuBinding[index].changeSateItemView(menuItems[index])
        itemsMenuBinding[previousItemSelected].changeSateItemView(menuItems[previousItemSelected])
        onMenuItemSelected?.invoke(index)
    }

    private fun LayoutItemBottomMenuBinding.changeSateItemView(menuItem: MenuItem) {
        (menuItem == menuItems[selectedItem]).apply {
            val color = if (this) colorSelected else colorUnSelected
            tvMenuTitle.setTextColor(color)
            imgMenuItem.tintImage(color)
            imgMenuItem.invisibleBy(this)
            lottieMenuView.invisibleBy(!this)
            if (this) lottieMenuView.playAnimation() else lottieMenuView.progress = 1f
        }


    }

    data class MenuItem(
        @RawRes val animation: Int? = null,
        @DrawableRes val drawable: Int? = null,
        val title: String
    )
}