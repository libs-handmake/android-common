package common.hoangdz.lib.datasouce

/**
 * Created by HoangDepTrai on 22, November, 2022 at 3:21 PM
 */
internal interface DataSource {
    suspend fun <T> launchData(call: () -> T): Result<T>
}