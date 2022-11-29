package common.hoangdz.lib.datasouce

import androidx.annotation.StringRes

/**
 * Created by HoangDepTrai on 22, November, 2022 at 3:13 PM
 */
sealed class Result<out T> {

    class Success<T>(val value: T) : Result<T>()

    class Failure(
        @StringRes val mgsRes: Int? = null,
        val mgsStr: String = "",
        val errorType: ErrorType
    ) : Result<Nothing>()

    enum class ErrorType {
        SERVER,
        ERROR_NET_WORK,
        OTHER
    }
}


