package com.example.neighborfriends.data.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * RetrofitHelper
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-03-27
 */

object RetrofitHelper {
    private const val DEFAULT_RETRIES = 3
    fun <T> enqueueWithRetry(call: Call<T>, retryCount: Int, callback: Callback<T>) {
        call.enqueue(object : RetryableCallback<T>(call, retryCount) {
            override fun onFinalResponse(call: Call<T>?, response: Response<T>?) {
                callback.onResponse(call, response)
            }

            override fun onFinalFailure(call: Call<T>?, t: Throwable?) {
                callback.onFailure(call, t)
            }
        })
    }

    fun <T> enqueueWithRetry(call: Call<T>, retryCount: Int?,  callback: Callback<T>) {
        enqueueWithRetry(call, retryCount ?: DEFAULT_RETRIES, callback)
    }

    @JvmStatic
    fun isCallSuccess(response: Response<*>): Boolean {
        val code = response.code()
        return code in 200..399
    }
}