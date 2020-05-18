package br.com.currencyconverter.repository


import br.com.currencyconverter.api.ApiResult
import br.com.currencyconverter.extras.Constants.Companion.NETWORK_ERROR
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import br.com.currencyconverter.extras.Constants.Companion.NETWORK_TIMEOUT
import br.com.currencyconverter.extras.Constants.Companion.NETWORK_ERROR_TIMEOUT
import br.com.currencyconverter.extras.Constants.Companion.UNKNOWN_ERROR
/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
):ApiResult<T> {
     return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT){
                val response = apiCall.invoke()

                ApiResult.Success(response)
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                    is TimeoutCancellationException -> {
                        val code = 408 // timeout error code
                        ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                    }
                    is IOException -> {
                        ApiResult.NetworkError(NETWORK_ERROR)
                    }
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ApiResult.GenericError(
                            code,
                            errorResponse
                        )
                    }
                    else -> {
                        ApiResult.GenericError(
                            null,
                            UNKNOWN_ERROR
                        )
                    }
                }
            }

        }
}


private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        UNKNOWN_ERROR
    }
}























