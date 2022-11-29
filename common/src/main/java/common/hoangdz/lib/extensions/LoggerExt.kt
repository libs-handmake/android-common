package common.hoangdz.lib.extensions

import android.util.Log

/**
 * Created by HoangDepTrai on 18, July, 2022 at 11:21 AM
 */

fun Any.logError(message: Any?) {
    Log.e("logError_" + javaClass.simpleName, "$message")
}