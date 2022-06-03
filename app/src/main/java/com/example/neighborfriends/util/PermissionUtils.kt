package com.example.neighborfriends.util

import com.example.neighborfriends.util.DataUtil.isNotNull
import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * PermissionUtils : 퍼미션 체크 관련 유틸
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */
object PermissionUtils {
    fun checkPermission(activity: Activity?, pList: Array<String?>, requestCode: Int): Boolean {
        for (i in pList.indices) {
            val perStatus = ContextCompat.checkSelfPermission(activity!!, pList[i]!!)
            if (perStatus == PackageManager.PERMISSION_DENIED) {
                requestPermission(activity, pList, requestCode)
                return false
            }
        }
        return true
    }

    fun checkPermission(activity: Activity?, pList: Array<String?>): Boolean {
        for (i in pList.indices) {
            val perStatus = ContextCompat.checkSelfPermission(activity!!, pList[i]!!)
            if (perStatus == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    fun requestPermission(activity: Activity?, pList: Array<String?>?, requestCode: Int) {
        BLog.d()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity!!, pList!!, requestCode)
        }
    }

    fun goAppSettingsActivity(context: Context) {
        BLog.d()
        if (isNotNull(context)) {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.packageName))
            context.startActivity(intent)
        }
    }
}