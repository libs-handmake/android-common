package common.hoangdz.lib.extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by HoangDepTrai on 16, July, 2022 at 3:24 PM
 */

fun JSONObject.getJsonObjectOrNull(key: String) = if (this.has(key)) getJSONObject(key) else null

fun JSONObject.getJsonArrayOrNull(key: String) = if (this.has(key)) getJSONArray(key) else null

fun JSONObject.getIntOrNull(key: String) = if (this.has(key)) getInt(key) else null

fun JSONObject.getStringOrNull(key: String) = if (this.has(key)) getString(key) else null

fun JSONObject.getStringArray(key: String) =
    if (this.has(key)) getString(key).createFromJson<Array<String>>()
        ?: arrayOf() else arrayOf()

fun JSONObject.getFloatOrNull(key: String) = if (this.has(key)) getString(key).toFloat() else null

fun JSONObject.getBooleanOrNull(key: String) =
    if (this.has(key)) getString(key).toBooleanStrictOrNull() else null

fun JSONObject.getLongOrNull(key: String) = if (this.has(key)) getLong(key) else null

fun JSONObject.getArrayFloatOrNull(key: String) =
    getStringOrNull(key).toString().createFromJson<Array<Float>>()

fun JSONObject.getIntArrayOrNull(key: String) =
    getStringOrNull(key).toString().createFromJson<Array<Int>>()

val JSONArray.indices
    get() = 0 until length()

operator fun JSONArray.get(index: Int) = get(index)


