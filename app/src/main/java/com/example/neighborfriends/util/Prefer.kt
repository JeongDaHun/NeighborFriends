package com.example.neighborfriends.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Prefer : 로컬스토리지에 저장
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */
object Prefer {
    const val PREF_LOGIN_TYPE = "pref_login_type"
    const val PREF_SERVER_APP_VERSION = "pref_server_app_version"

    /**
     * SharedPreference get
     *
     */
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    }

    /**
     * SharedPreference clear
     *
     */
    fun clearPreference(context: Context, key: String?) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.remove(key).apply()
    }

    /**
     * String 형식 저장하기 위한 함수
     *
     */
    fun getStringPreference(context: Context, key: String?): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(key, "")
    }

    fun setStringPreference(context: Context, key: String?, `val`: String?) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, `val`)
        editor.apply()
    }

    /**
     * Boolean 형식 저장하기 위한 함수
     *
     */
    fun getBooleanPreference(context: Context, key: String?): Boolean {
        val prefs = getSharedPreferences(context)
        return prefs.getBoolean(key, false)
    }

    fun setBooleanPreference(context: Context, key: String?, `val`: Boolean?) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(key, `val`!!)
        editor.apply()
    }

    /**
     * Integer 형식 저장하기 위한 함수
     *
     */
    fun getIntegerPreference(context: Context, key: String?): Int {
        val prefs = getSharedPreferences(context)
        return prefs.getInt(key, -1)
    }

    fun setIntegerPreference(context: Context, key: String?, `val`: Int?) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, `val`!!)
        editor.apply()
    }

    /**
     * 서버 버전
     *
     */
    fun getServerAppVersion(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(PREF_SERVER_APP_VERSION, "0.0.0")
    }

    fun setServerAppVersion(context: Context, version: String?) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(PREF_SERVER_APP_VERSION, version)
        editor.apply()
    }
}