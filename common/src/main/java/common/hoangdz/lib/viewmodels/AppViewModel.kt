package common.hoangdz.lib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import common.hoangdz.lib.extensions.launchIO

/**
 * Created by HoangDepTrai on 22, November, 2022 at 3:28 PM
 */
open class AppViewModel(application: Application) : AndroidViewModel(application) {

    protected val _loadingProgress by lazy { MutableLiveData<Int>() }
    val loadingProgress: LiveData<Int>
        get() = _loadingProgress

    fun <T> launchData(call: suspend () -> T) {
        _loadingProgress.postValue(0)
        viewModelScope.launchIO {
            call()
            _loadingProgress.postValue(100)
        }
    }

}