@file:Suppress("UNCHECKED_CAST")

package common.hoangdz.lib.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.widget.ImageViewCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import common.hoangdz.lib.CommonApp
import common.hoangdz.lib.R
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


fun View.setOnClickCheckInternet(
    context: Context,
    throttleDelay: Long = 100L,
    onClick: (View) -> Unit,
) {
    setOnClickListener {
//        if (NetworkHelper.isConnected(context)) {
//            onClick(this)
//            isClickable = false
//            postDelayed({ isClickable = true }, throttleDelay)
//        } else {
//            Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show()
//        }

    }
}

fun View.onAvoidDoubleClick(
    throttleDelay: Long = 600L,
    onClick: (View) -> Unit,
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun View.setAnimation(context: Activity, anim: Int?, duration: Long, onAnimation: (View) -> Unit) {
    this.startAnimation(anim?.let { AnimationUtils.loadAnimation(context, it) })
    Timer().schedule(duration) {
        context.runOnUiThread {
            onAnimation(this@setAnimation)
        }

    }
}

fun View.visible() {
    if (visibility != View.VISIBLE)
        this.visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE)
        this.visibility = View.INVISIBLE
}

fun View.gone() {
    if (visibility != View.GONE)
        this.visibility = View.GONE
}

@ColorInt
fun View.getColorR(@ColorRes res: Int): Int {
    return context.getColorR(res)
}

fun View.drawable(@DrawableRes res: Int): Drawable? {
    return context.drawable(res)
}

fun View.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    return context.tintedDrawable(drawableId, colorId)
}

fun View.string(@StringRes res: Int): String {
    return context.string(res)
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.displaySnackBarWithBottomMargin(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit,
    sideMargin: Int,
    marginBottom: Int
) {
    val snack = Snackbar.make(this, message, length)
    val snackBarView = snack.view
    val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
    params.setMargins(
        params.leftMargin + sideMargin,
        params.topMargin,
        params.rightMargin + sideMargin,
        params.bottomMargin + marginBottom
    )
    snackBarView.layoutParams = params
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.makeLinksColor(color: Int, vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = color
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun ImageView.ofHeight(): Int {
    return this.drawable.intrinsicHeight
}

fun ImageView.ofWidth(): Int {
    return this.drawable.intrinsicWidth
}

fun Drawable.setTint(color: Int) {
    colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
}

fun ImageView.setTint(colorRes: Int) {
    try {
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
        )
    } catch (e: Exception) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorRes))
    }
}

fun View.setMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        this.requestLayout()
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.setOnLongClickHandle(handler: Handler, listener: () -> Unit) {
    setOnTouchListener { v, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                listener.invoke()
            }
            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacksAndMessages(null)
            }
            MotionEvent.ACTION_UP -> {
                handler.removeCallbacksAndMessages(null)
            }
        }
        true
    }
}

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun View.clickNoAnim(onClick: (View) -> Unit) {
    if (background == null) {
        setBackgroundResource(R.drawable.bg_ripple)
    }
    this.setOnClickListener {
        onClick(this)
    }
}


fun View.visibleBy(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.invisibleBy(visible: Boolean) {
    if (visible) invisible() else visible()
}

fun View.doOnViewDrawn(removeCallbackBy: (() -> Boolean) = { true }, onDrawn: () -> Unit) {
    var global: ViewTreeObserver.OnGlobalLayoutListener? = null
    global = ViewTreeObserver.OnGlobalLayoutListener {
        if (removeCallbackBy()) {
            onDrawn()
            viewTreeObserver.removeOnGlobalLayoutListener(global)
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(global)
}

fun View.doOnViewDrawnRepeat(removeCallbackBy: (() -> Boolean) = { true }, onDrawn: () -> Unit) {
    var global: ViewTreeObserver.OnGlobalLayoutListener? = null
    global = ViewTreeObserver.OnGlobalLayoutListener {
        onDrawn()
        if (removeCallbackBy()) {
            viewTreeObserver.removeOnGlobalLayoutListener(global)
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(global)
}

fun ImageView.tintImage(color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

@Keep
fun <VB : ViewBinding> Any.inflateViewBinding(
    inflater: LayoutInflater,
    viewGroup: ViewGroup? = null,
    isAttachToRoot: Boolean = false,
    genericArgumentIndex: Int = 0
): VB {
    val clazz =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[genericArgumentIndex] as Class<VB>
    return clazz.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
        .invoke(null, inflater, viewGroup, isAttachToRoot) as VB
}

fun View.toBitMap(): Bitmap? {
    if (measuredWidth * measuredHeight == 0) return null
    val b = Bitmap.createBitmap(
        measuredWidth,
        measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val c = Canvas(b)
    layout(left, top, right, bottom)
    draw(c)
    return b
}

fun View.listenKeyBoardToggle(onKeyBoardToggle: (Boolean) -> Unit) {
    doOnViewDrawnRepeat({ false }) {
        val r = Rect()
        getWindowVisibleDisplayFrame(r)
        val screenHeight: Int = rootView.height
        val keypadHeight: Int = screenHeight - r.bottom
        onKeyBoardToggle(keypadHeight > screenHeight * 0.15)
    }
}


inline fun <reified C> Context.getResID(resName: String): Int {
    return try {
        val idField: Field = C::class.java.getDeclaredField(resName)
        idField.getInt(idField)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        -1
    }
}

fun View.hostedActivity() = kotlin.run {
    var check = context
    while (check is ContextWrapper) {
        if (check is Activity) {
            return@run check
        }
        check = check.baseContext
    }
    return@run null
}

fun ImageView.loadGlide(
    src: Any,
    onRequestManager: (RequestManager.() -> Unit)? = null,
    onRequestBuilder: (RequestBuilder<Drawable>.() -> Unit)? = null
) = Glide.with(this).also { onRequestManager?.invoke(it) }.load(src)
    .also { onRequestBuilder?.invoke(it) }.into(this)

val Int.dpInt
    get() = CommonApp.instance?.let {
        it.dimenInt(it.getResID<com.intuit.sdp.R.dimen>("_${this}sdp"))
    } ?: 0

val Int.dpFloat
    get() = CommonApp.instance?.let {
        it.dimenFloat(it.getResID<com.intuit.sdp.R.dimen>("_${this}sdp"))
    } ?: 0

