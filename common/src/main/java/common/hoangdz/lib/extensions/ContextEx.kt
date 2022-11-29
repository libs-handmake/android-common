package common.hoangdz.lib.extensions

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Vibrator
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import common.hoangdz.lib.components.BaseActivity
import dagger.hilt.android.EntryPointAccessors
import kotlin.math.abs


fun Context.getDisplayMetrics(): DisplayMetrics {
    return DisplayMetrics().apply {
        getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)
    }
}

fun Context.getDisplayWidth() = getDisplayMetrics().widthPixels

fun Context.getDisplayHeight() = getDisplayMetrics().heightPixels

fun Context.dpToPx(dp: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics
).toInt()

fun Context.toastMsg(msg: Int) =
    Toast.makeText(this, this.getString(msg), Toast.LENGTH_SHORT).show()

fun Context.toastMsg(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

@ColorInt
fun Context.getColorR(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    val tint: Int = getColorR(colorId)
    val drawable = drawable(drawableId)
    drawable?.mutate()
    drawable?.let {
        it.mutate()
        DrawableCompat.setTint(it, tint)
    }
    return drawable
}

fun Context.string(@StringRes res: Int): String {
    return getString(res)
}

fun Context.hideSoftKeyboard(v: View) {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    v.clearFocus()
}

fun Context.showSoftKeyboard() {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Dialog.showSoftKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.showSoftKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideSoftKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Dialog.hideSoftKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun AppCompatActivity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            0
        )
    }
}


fun Fragment.hideSoftKeyboard() {
    val inputMethodManager = activity?.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager?
    if (inputMethodManager?.isAcceptingText == true) {
        inputMethodManager.hideSoftInputFromWindow(
            activity?.currentFocus?.windowToken ?: return,
            0
        )
    }
}


inline fun <reified T : Activity> Context.runActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

inline fun <reified T : Activity> Fragment.runActivity(block: Intent.() -> Unit = {}) {
    requireActivity().runActivity<T>(block)
}

fun Context.copyClipboard(text: String, label: String = "") {
    val clipboard: ClipboardManager =
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}


fun Context.openLink(url: String, haveAnError: () -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        haveAnError.invoke()
        e.printStackTrace()
    }
}

fun Context.searchGoogle(text: String) {
    val uri = Uri.parse("http://www.google.com/search?q=$text")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        openLink(text) {

        }
    }
}

fun Context.searchEbay(text: String) {
    val url = "https://www.ebay.com/sch/i.html?_nkw=$text"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.searchAmazon(text: String) {
    val url = "https://www.amazon.com/s?k=$text"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.searchWeb(text: String) {
    val intent = Intent(Intent.ACTION_WEB_SEARCH)

    intent.putExtra("query", text)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


val Context.vibrator: Vibrator?
    get() = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

val Context.wifiManager: WifiManager?
    get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

inline fun <reified T> Context.appInject() =
    EntryPointAccessors.fromApplication(this, T::class.java)

inline fun <reified T> Activity.activityInject() =
    EntryPointAccessors.fromActivity(this, T::class.java)

fun Context.dimenInt(@DimenRes dimen: Int) = resources.getDimensionPixelSize(dimen)

fun Context.dimenFloat(@DimenRes dimen: Int) = resources.getDimension(dimen)

fun View.dimenInt(@DimenRes dimen: Int) = resources.getDimensionPixelSize(dimen)

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.linkAppStore() {
    val uri: Uri = Uri.parse("market://details?id=${this.packageName}")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        this.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=${this.packageName}")
            )
        )
    }
}

fun BaseActivity<*>.makeFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
}

fun BaseActivity<*>.exitFullScreenMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    } else
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
}

val Activity.statusBarHeight: Int
    get() {
        val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight: Int = rectangle.top
        val contentViewTop: Int = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        return abs(contentViewTop - statusBarHeight)
    }

val Activity.navigationBarHeight: Int
    get() {
        val resources = resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

val Context.screenSize
    get() = resources.displayMetrics.let { it.widthPixels to it.heightPixels }