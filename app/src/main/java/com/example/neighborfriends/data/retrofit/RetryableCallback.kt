package com.example.neighborfriends.data.retrofit

import com.example.neighborfriends.data.retrofit.RetrofitHelper.isCallSuccess
import com.example.neighborfriends.util.BLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * RetryableCallback
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-03-27
 */

abstract class RetryableCallback<T>(private val call: Call<T>, totalRetry: Int) : Callback<T> {
    private var totalRetry = 3
    private var retryCount = 0
    override fun onResponse(call: Call<T>, response: Response<T>) {
        BLog.d("response = $response")
        if (!isCallSuccess(response)) {
            if (retryCount++ < totalRetry) {
                retry()
            } else {
                onFinalResponse(call, response)
            }
        } else {
            onFinalResponse(call, response)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        BLog.d("call = " + call + "t = " + t)
        if (retryCount++ < totalRetry) {
            retry()
        } else {
            onFinalFailure(call, t)
        }
    }

    open fun onFinalResponse(call: Call<T>?, response: Response<T>?) {}
    open fun onFinalFailure(call: Call<T>?, t: Throwable?) {}
    private fun retry() {
        BLog.d("RetryableCallback :: retry = $retryCount")
        call.clone().enqueue(this)
    }

    init {
        this.totalRetry = totalRetry
    }
}