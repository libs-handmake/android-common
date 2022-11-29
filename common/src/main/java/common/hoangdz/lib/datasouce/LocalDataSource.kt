package common.hoangdz.lib.datasouce

/**
 * Created by HoangDepTrai on 22, November, 2022 at 3:22 PM
 */
open class LocalDataSource : DataSource {

    override suspend fun <T> launchData(call: () -> T): Result<T> {
        return try {
            Result.Success(call.invoke())
        } catch (e: Throwable) {
            Result.Failure(errorType = Result.ErrorType.OTHER)
        }
    }


}