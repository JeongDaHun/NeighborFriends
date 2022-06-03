package com.example.neighborfriends

import android.app.Activity
import android.app.Application
import android.content.Context

/**
 * NeighborFriendsApplication
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-03-27
 */

class NeighborFriendsApplication: Application() {

    private val mInstance: NeighborFriendsApplication? = null
    private val mActivity: Activity? = null
    private var mTelNo: String? = null
    private val mAppContext: Context? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    /**
     * 웹에서 전화걸기 시도시 전화번호 임시저장
     *
     * @param telNo 전화번호
     */
    fun setTempTelNo(telNo: String) {
        this.mTelNo = telNo
    }

    fun getTempTelNo(): String? {
        return mTelNo
    }
}