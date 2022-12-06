package common.hoangdz.lib.utils

import android.content.Context
import common.hoangdz.lib.extensions.createFromJson
import common.hoangdz.lib.extensions.toJson

abstract class PreferenceHelper(protected val context: Context) {

    abstract fun getPrefName(): String

    protected val pref by lazy { context.getSharedPreferences(getPrefName(), Context.MODE_PRIVATE) }

    protected val editor by lazy { pref.edit() }

    protected fun putInt(key: String, value: Int) = editor.putInt(key, value).apply()

    protected fun putString(key: String, value: String) = editor.putString(key, value).apply()

    protected fun putBoolean(key: String, value: Boolean) = editor.putBoolean(key, value).apply()

    protected fun putLong(key: String, value: Long) = editor.putLong(key, value).apply()

    protected fun putFloat(key: String, value: Float) = editor.putFloat(key, value).apply()

    protected fun putObject(key: String, value: Any) = putString(key, value.toJson())

    protected inline fun <reified T> getObject(key: String) =
        pref.getString(key, "{}")?.createFromJson<T>()

}