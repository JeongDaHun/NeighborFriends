package com.example.neighborfriends.data.retrofit

import com.example.neighborfriends.data.model.LoginModel
import com.example.neighborfriends.data.model.LottoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * RetrofitApiService
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-03-27
 */

interface RetrofitApiService {
    companion object {
        const val AES256ContentEncoding= "Content-encoding: AES256"
        const val ContentType = "Content-Type: application/json;charset=utf-8"

        const val LOTTO_INFO_URL = "/common.do?method=getLottoNumber"
    }

    //로도 정보 검색
    @Headers(ContentType)
    @GET(LOTTO_INFO_URL)
    fun getLottoInfo(@Query("drwNo") drwNo: Int): Call<LottoModel>

    fun requestLogin(@Query("id") id: String, @Query("pw") pw: String): Call<LoginModel>
}