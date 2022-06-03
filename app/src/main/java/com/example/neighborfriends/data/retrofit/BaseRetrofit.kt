package com.example.neighborfriends.data.retrofit

import android.webkit.CookieManager
import com.example.neighborfriends.BuildConfig
import com.example.neighborfriends.data.model.LoginModel
import com.example.neighborfriends.data.model.LottoModel
import com.example.neighborfriends.data.retrofit.RetrofitHelper.enqueueWithRetry
import com.example.neighborfriends.util.BLog
import com.example.neighborfriends.util.DataUtil
import com.example.neighborfriends.util.Prefer
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * BaseRetrofit
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-03-27
 */

class BaseRetrofit(private val listener: RetrofitListener) {
    private var mRetrofit: Retrofit? = null
    private var mRetrofitService: RetrofitApiService? = null

    companion object {
        private const val baseUrl = " https://www.dhlottery.co.kr/"
        private const val dev_baseUrl = " https://www.dhlottery.co.kr/"
        private const val RETRY_COUNT = 3
    }

    init {
        val gson = GsonBuilder().setLenient().create()
        if (BuildConfig.DEBUG) {
            mRetrofit = Retrofit.Builder()
                .baseUrl(dev_baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            mRetrofitService = mRetrofit?.create(RetrofitApiService::class.java)
        } else {
            mRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            mRetrofitService = mRetrofit?.create(RetrofitApiService::class.java)
        }
    }

    interface RetrofitListener {
        fun onComplete(data: Any?, url: String?)
        fun onFail(errCode: Int, url: String?)
        fun onFail(message: String?, url: String?)
    }

    fun saveCookie(response: Response<String>) {
        val cookie: String = response.headers().get("Set-Cookie").toString()
        if (DataUtil.isNotNull(cookie)) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setCookie(response.raw().request().url().toString().split("?").first(), cookie)
            cookieManager.flush()
        }
    }

    //로또 조회
        fun getLotto(drwNo: Int) {
        val call: Call<LottoModel> = mRetrofitService!!.getLottoInfo(drwNo=drwNo)
        BLog.d("getMsgCntList :: "+ "URL = " + call.request())
        DataUtil.checkNull("dd")
        enqueueWithRetry(call, null, object : Callback<LottoModel> {
            override fun onResponse(call: Call<LottoModel>, response: Response<LottoModel>
            ) {
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), call.request().url().toString())
                } else {
                    listener.onFail(response.code(), call.request().url().toString())
                }
            }

            override fun onFailure(call: Call<LottoModel>, t: Throwable) {
                listener.onFail(t.toString(), call.request().url().toString())
            }
        })
    }

    fun requsetLogin(id: String, pw: String) {
        val call: Call<LoginModel> = mRetrofitService!!.requestLogin(id = id, pw = pw)

        enqueueWithRetry(call, null, object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), call.request().url().toString())
                } else {
                    listener.onFail(response.code(), call.request().url().toString())
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                listener.onFail(t.toString(), call.request().url().toString())
            }

        })
    }
}