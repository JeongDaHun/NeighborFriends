package com.example.neighborfriends.util

import android.Manifest
import com.example.neighborfriends.util.DataUtil.isNotNull
import com.example.neighborfriends.util.DataUtil.isNull
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.app.Activity
import androidx.core.app.ActivityCompat
import com.example.neighborfriends.NeighborFriendsApplication
import android.content.Intent
import android.text.TextUtils
import android.util.DisplayMetrics
import android.os.Build
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import android.widget.TextView
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import androidx.core.app.NotificationManagerCompat
import android.view.accessibility.AccessibilityManager
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.SystemClock
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import android.view.Gravity
import android.view.View
import org.json.JSONObject
import org.json.JSONException
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * Utils : 기본 유틸
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */

object Utils {
    /**
     * 앱 버전명을 가져온다.
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        return try {
            val i = context.packageManager.getPackageInfo(context.packageName, 0)
            i.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    /**
     * 앱 버전 코드를 가져온다.
     *
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): Int {
        var code = 0
        try {
            val i = context.packageManager.getPackageInfo(context.packageName, 0)
            code = i.versionCode

//            Logs.e("version code : " + code);
            BLog.e("version code : $code")
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return code
    }

    /**
     * 해당 기기의 통신사를 반환한다
     *
     * @param context
     * @return String
     */
    fun getOperator(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var operator = tm.simOperatorName
        if (operator == null || operator.length <= 0) operator = tm.networkOperatorName
        return operator
    }

    /**
     * 전화를 건다.
     *
     * @param activity
     * @param telNo    전화번호 문자열
     */
    fun makeCall(activity: Activity, telNo: String) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            (activity.applicationContext as NeighborFriendsApplication).setTempTelNo(telNo)
            val perList = arrayOf(Manifest.permission.CALL_PHONE)
            ActivityCompat.requestPermissions(activity, perList, Const.REQUEST_PERMISSION_CALL)
        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$telNo"))
            activity.startActivity(intent)
        }
    }

    /**
     * 오늘 날짜를 가져온다.
     *
     * @param gubun 년월일의 구분문자열
     * @return
     */
    fun getCurrentDate(gubun: String): String {
        val mCalendar: Calendar = GregorianCalendar()
        var currentTime: String
        val year = mCalendar[Calendar.YEAR]
        val month = mCalendar[Calendar.MONTH] + 1 //월은 0~11까지 그래서 1을 더해줘야함.
        val dayOfMonth = mCalendar[Calendar.DAY_OF_MONTH]
        val strMonth = String.format("%02d", month)
        val strDay = String.format("%02d", dayOfMonth)
        return if (!TextUtils.isEmpty(gubun)) year.toString() + gubun + strMonth + gubun + strDay else year.toString() + strMonth + strDay
    }

    /**
     * 이전달, 다음달 년월을 가져온다.
     *
     * @param add_month
     * @return
     */
    fun getAddMonth(add_month: Int): String {
        val mCalendar: Calendar = GregorianCalendar()
        mCalendar.add(Calendar.MONTH, add_month)
        val year = mCalendar[Calendar.YEAR]
        val month = mCalendar[Calendar.MONTH] + 1 //월은 0~11까지 그래서 1을 더해줘야함.
        val dayOfMonth = mCalendar[Calendar.DAY_OF_MONTH]
        val strMonth = String.format("%02d", month)
        val strDay = String.format("%02d", dayOfMonth)
        return year.toString() + strMonth
    }

    /**
     * 현재 시간을 가져온다.
     *
     * @param gubun 년월일의 구분문자열
     * @return
     */
    fun getCurrentTime(gubun: String): String {
        val mCalendar: Calendar = GregorianCalendar()
        var currentTime: String
        val hour = mCalendar[Calendar.HOUR_OF_DAY]
        val min = mCalendar[Calendar.MINUTE]
        val sec = mCalendar[Calendar.SECOND]
        val strHour = String.format("%02d", hour)
        val strMin = String.format("%02d", min)
        val strSec = String.format("%02d", sec)
        return if (!TextUtils.isEmpty(gubun)) strHour + gubun + strMin + gubun + strSec else strHour + strMin + strSec
    }

    /**
     * Convert byte -> hex
     *
     * @param data byte array
     * @return hex String
     */
    fun bytesToHex(data: ByteArray?): String? {
        if (data == null || data.size == 0) return null
        val sb = StringBuilder()
        for (b in data) sb.append(String.format("%02x ", b and 0xff.toByte()))
        return sb.toString()
    }

    /**
     * Convert hex -> byte
     *
     * @param str Hex String
     * @return byte arry
     */
    fun hexToBytes(str: String?): ByteArray? {
        if (str == null || str.length < 2) {
            return null
        }
        val buffer = ByteArray(str.length / 2)
        for (i in buffer.indices) {
            buffer[i] = str.substring(i * 2, i * 2 + 2).toInt(16).toByte()
        }
        return buffer
    }

    /**
     * 앱의 캐시를 지운다. 모든 캐쉬를 지우면 문제가 되니 필요한것만 지음.
     *
     * @param context
     */
    fun clearAppCash(context: Context) {
        val cache = context.cacheDir
        val cachedir = File(cache.parent)
        if (cachedir.exists()) {
            //더 자세히 작성하도록.
            val dirs = cachedir.list()
            for (s in dirs) {
                if (s == "cache" || s == "app_appcache") {
                    Files.deleteDir(File(cachedir, s))
                }
            }
        }
    }

    fun clearAppData(context: Context) {
        val cache = context.cacheDir //캐시 폴더 호출
        val appDir = File(cache.parent) //App Data 삭제를 위해 캐시 폴더의 부모폴더까지 호출
        if (appDir.exists()) {
            val children = appDir.list()
            for (s in children) {
                //App Data 폴더의 리스트를 deleteDir 를 통해 하위 디렉토리 삭제
                if (s.toLowerCase().contains("webview")) {
                    val a = deleteDir(File(appDir, s))
                }
            }
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()

            //파일 리스트를 반복문으로 호출
            for (i in children.indices) {
                if (children[i] != "app_webview" && !children[i].contains("variations_stamp")) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
        }

        //디렉토리가 비어있거나 파일이므로 삭제 처리
        return dir!!.delete()
    }

    /**
     * 앱의 모든 캐시를 지운다.Prefer,Database등등 모두 지워버림.
     *
     * @param context
     */
    fun clearAllCash(context: Context) {
        val cache = context.cacheDir
        val cachedir = File(cache.parent)
        if (cachedir.exists()) {
            //더 자세히 작성하도록.
            val dirs = cachedir.list()
            for (s in dirs) {
                Files.deleteDir(File(cachedir, s))
            }
        }
    }

    fun dpToPixel(c: Context, dp: Float): Float {
        if (dp == 0f) return 0F
        val density = c.resources.displayMetrics.density
        return dp * density
    }

    fun pixelToDp(context: Context, px: Float): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun strConcat(vararg arg: String?): String {
        val sb = StringBuilder()
        for (str in arg) {
            sb.append(str)
        }
        return sb.toString()
    }

    fun isInstalledV3MobilePlus(ctx: Context): Boolean {
        var result = true
        val packMgr = ctx.packageManager
        try {
            packMgr.getPackageInfo("com.ahnlab.v3mobileplus", PackageManager.GET_CONFIGURATIONS)
        } catch (e: PackageManager.NameNotFoundException) {
            result = false
        }
        return result
    }

    fun isValidatePwPattern(target: String, userId: String?, minLen: Int, maxLen: Int): Boolean {
//        var pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z])."
//        pwPattern += "{"
//        pwPattern += minLen
//        pwPattern += ","
//        pwPattern += maxLen
//        pwPattern += "}$"
        val pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{$minLen,$maxLen}$"

        // 정규식 (영문(대소문자 구분), 숫자, 특수문자 조합, 9~12자리)
        val matcher = Pattern.compile(pwPattern).matcher(target)
        if (!matcher.matches()) {
            return false
        }

        // 정규식 (같은 문자 4개 이상 사용 불가)
        val pwRepeat = "(.)\\1\\1\\1"
        val matcher2 = Pattern.compile(pwRepeat).matcher(target)
        if (matcher2.find()) return false

        // 아이디 포함 여부
        if (target.contains(userId!!)) return false

        // 공백문자 포함 여부
        return if (target.contains(" ")) false else true
    }
    /*
     * 이체금액 숫자로 입력시 자동으로 한글로 변경
     * */
    /*
    public static String convertHangul(String moneyStr) {
        String[] han = {"", "만", "억", "조", "경"};

        if (moneyStr.contains(",")) {
            moneyStr = moneyStr.replace(",", "");
        }

        StringBuffer result = new StringBuffer();

        int len = moneyStr.length();
        for (int i = len - 1; i >= 0; i--) {
            result.append(moneyStr.substring(len - i - 1, len - i));
            if (Integer.parseInt(moneyStr.substring(len - i - 1, len - i)) > 0) {
                if (i % 4 == 3)
                    result.append(",");
            }

            if (i % 4 == 0) {
                result.append(han[i / 4]);
            }
        }
        return result.toString();
    }
    */
    /**
     * 이체금액 숫자로 입력시 자동으로 한글로 변경 : 1000억단위까지 가능
     *
     * @param moneyStr 금액 문자열
     * @return 한글 금액 문자열
     */
    fun convertHangul(moneyStr: String): String {
        var moneyStr = moneyStr
        val result = StringBuffer()
        if (moneyStr.contains(",")) {
            moneyStr = moneyStr.replace(",", "")
        }
        val len = moneyStr.length
        if (len < 5) {
            if (moneyStr.length == 4) {
                moneyStr = moneyStr.substring(0, 1) + "," + moneyStr.substring(1, 4)
            }
            result.append(moneyStr)
        } else if (len < 9) {
            var partialStr = moneyStr.substring(0, len - 4)
            if (partialStr.length == 4) {
                partialStr = partialStr.substring(0, 1) + "," + partialStr.substring(1, 4)
            }
            result.append(partialStr + "만")
            partialStr = moneyStr.substring(len - 4, len)
            val moneyInt = partialStr.toInt()
            if (moneyInt != 0) {
                partialStr = moneyInt.toString()
                if (partialStr.length == 4) {
                    partialStr = partialStr.substring(0, 1) + "," + partialStr.substring(1, 4)
                }
                result.append(partialStr)
            }
        } else {
            var partialStr = moneyStr.substring(0, len - 8)
            if (partialStr.length == 4) {
                partialStr = partialStr.substring(0, 1) + "," + partialStr.substring(1, 4)
            }
            result.append(partialStr + "억")
            partialStr = moneyStr.substring(len - 8, len - 4)
            var moneyInt = partialStr.toInt()
            if (moneyInt != 0) {
                partialStr = moneyInt.toString()
                if (partialStr.length == 4) {
                    partialStr = partialStr.substring(0, 1) + "," + partialStr.substring(1, 4)
                }
                result.append(partialStr + "만")
            }
            partialStr = moneyStr.substring(len - 4, len)
            moneyInt = partialStr.toInt()
            if (moneyInt != 0) {
                partialStr = moneyInt.toString()
                if (partialStr.length == 4) {
                    partialStr = partialStr.substring(0, 1) + "," + partialStr.substring(1, 4)
                }
                result.append(partialStr)
            }
        }
        return result.toString()
    }

    /*
     * 에디트 텍스트에서 키보드 보이기
     */
    fun showKeyboard(context: Context?, target: View?) {
        if (context == null || target == null) {
            return
        }
        val imm = getInputMethodManager(context)
        imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
    }

    /*
     * 에디트 텍스트에서 키보드 감추기
     */
    fun hideKeyboard(context: Context?, target: View?) {
        if (context == null || target == null) {
            return
        }
        val imm = getInputMethodManager(context)
        imm.hideSoftInputFromWindow(target.windowToken, 0)
    }

    private fun getInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun finishAffinity(activity: Activity) {
        activity.setResult(Activity.RESULT_CANCELED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAffinity()
        }
    }

    /**
     * 현재 기기의 전화번호 가져오기
     *
     * @param context
     * @return String
     */
    @SuppressLint("HardwareIds")
    fun getPhoneNumber(context: Context): String {
        var permission = ""
        //api30 이상인경우
        permission = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Manifest.permission.READ_PHONE_NUMBERS
        } else {
            Manifest.permission.READ_PHONE_STATE
        }
        val mTelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            && isNotNull(mTelephonyManager)
            && isNotNull(mTelephonyManager.line1Number)
            && mTelephonyManager.line1Number.length >= 10
        ) {
            mTelephonyManager.line1Number.replace("+82", "0").replace("-", "")
        } else ""
    }

    /**
     * 현재 기기의 Sim카드 존재 여부
     *
     * @param context
     */
    @SuppressLint("MissingPermission")
    fun isExistSimCard(context: Context): Int {
        var permission = ""
        //api30 이상인경우
        permission = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Manifest.permission.READ_PHONE_NUMBERS
        } else {
            Manifest.permission.READ_PHONE_STATE
        }
        return if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val tManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (tManager.simState == TelephonyManager.SIM_STATE_UNKNOWN || // 유심 상태를 알 수 없는 경우
                tManager.simState == TelephonyManager.SIM_STATE_ABSENT || // 유심이 없는 경우
                tManager.simState == TelephonyManager.SIM_STATE_PERM_DISABLED || // 유심이 존재하지만, 사용 중지 상태인 경우
                tManager.simState == TelephonyManager.SIM_STATE_CARD_IO_ERROR || // 유심이 존재하지만, 오류 상태(결함)인 경우
                tManager.simState == TelephonyManager.SIM_STATE_CARD_RESTRICTED
            ) { // 유심이 존재하지만, 통신사 제한으로 사용 불가 상태인 경우
                1 // 유심 문제
            } else {
                0 // 정상
            }

//            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//            if (sManager == null)
//                return false;
//
//            List<SubscriptionInfo> clsList = sManager.getActiveSubscriptionInfoList();
//
//            if(clsList == null || clsList.isEmpty()) return false;
//
//            Logs.e("isExistSimCard : clsList : " + clsList.toString());
//
//            return true;


//            SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
//            SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
//            if(infoSim1 != null || infoSim2 != null)
//                return true;
        } else {
            -1 // 권한 거부됨.
        }
        //        return false;
    }

    /**
     * 현재 기기의 USB 디버깅 모드 활성화 상태 가져오기
     *
     * @param context
     */
    fun isUsbDebuggingEnable(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) != 0
    }

    /**
     * 현재 기기의 개발자옵션 메뉴 활성화 상태 가져오기
     *
     * @param context
     */
    fun isDevelopmentSettingsEnable(context: Context): Boolean {
        return Settings.Secure.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) != 0
    }

    /**
     * 이름 유효성 체크(키보드 입력 후 완성형 한글만)
     *
     * @param strname 이름
     * @return STATE_VALID_STR_NORMAL : 정상, STATE_VALID_STR_LENGTH_ERR : 길이 오류, STATE_VALID_STR_CHAR_ERR : 한글아님 오류
     */
    fun isKorean(strname: String): Int {
//        Logs.i("isKorean");
        BLog.i("isKorean")
        return if (strname.isNotEmpty()) {
            if (strname.matches(Regex("^[가-힣]*$"))) {  // 완성형 한글 입력 ok
                if (strname.length in 2..14) {
                    Const.STATE_VALID_STR_NORMAL
                } else {
                    Const.STATE_VALID_STR_LENGTH_ERR
                }
            } else {
                Const.STATE_VALID_STR_CHAR_ERR
            }
        } else {
            Const.STATE_VALID_STR_NORMAL
        }
    }

    /**
     * double형 원화 표시
     *
     * @param inputMoney 금액
     * @return
     */
    fun moneyFormatToWon(inputMoney: Double?): String {
        var inputMoney = inputMoney
        if (inputMoney == null) inputMoney = 0.0
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(inputMoney)
    }

    /**
     * 특정 글자를 스타일 변경하여 출력
     *
     * @param textView 텍스트 뷰
     * @param text     전체 문자
     * @param spanText 변경할 글자
     */
    fun setTextWithSpan(
        textView: TextView,
        text: String,
        spanText: String,
        typeface: Int,
        colorStr: String?
    ) {
        val span = SpannableStringBuilder(text)
        if (!TextUtils.isEmpty(spanText)) {
            val start = text.indexOf(spanText)
            val end = start + spanText.length
            if (start < 0) {
                textView.text = text
                return
            }
            span.setSpan(StyleSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (!TextUtils.isEmpty(colorStr)) {
                val color = Color.parseColor(colorStr)
                span.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            textView.text = span
        } else {
            textView.text = text
        }
    }

    /**
     * 앱 사인키 해쉬값 가져오기
     *
     * @param context
     * @return
     */
    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES)
            ?: return null
        for (signature in packageInfo.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                //                Logs.printException(e);
            }
        }
        return null
    }

    fun getPackageInfo(context: Context, flag: Int): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, flag)
        } catch (e: PackageManager.NameNotFoundException) {
//            Log.w(TAG, "Unable to get PackageInfo", e);
            BLog.w("Unable to get PackageInfo", e)
        }
        return null
    }

    /**
     * 문장 내, 같은 글자 반복 확인
     *
     * @param target    전체문장
     * @param duplicate 반복 횟수
     * @return
     */
    fun isDuplicate(target: String, duplicate: Int): Boolean {
        for (index in 0 until target.length) {
            val toCheck = target[index]
            var countCheck = 0
            var isStart = false
            for (ch in target.toCharArray()) {
                if (ch == toCheck) {
                    if (!isStart) isStart = true
                    if (isStart) countCheck++
                } else {
                    isStart = false
                    countCheck = 0
                }
                if (countCheck == duplicate) return true
            }
        }
        return false
    }

    /**
     * 문장 내, 세숫자가 연속적인 숫자인지 확인
     *
     * @param target 전체문장
     * @param limit  체크할 연속 숫자 길이
     * @return limit길이만큼 연속된 숫자가 있으면 true
     */
    fun isContinueNum(target: String, limit: Int): Boolean {
        var o = 0
        var d = 0
        var p = 0
        var n = 0

        // 체크할 문자길이가 최소 3자 이상이어야 함
        if (limit < 3) return false
        for (i in 0 until target.length) {
            val c = target[i]
            p = o - c.toInt()
            if (i > 0 && (p == -1 || p == 1) && (if (p == d) n + 1 else 0.also {
                    n = it
                }) > limit - 3) return true
            d = p
            o = c.toInt()
        }
        return false
    }

    fun isInstallApp(context: Context, uri: String?): Boolean {
        val pm = context.packageManager
        var appInstalled = false
        appInstalled = try {
            pm.getPackageInfo(uri!!, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }

    /**
     * 앱 백그라운드 상태 체크
     *
     * @param context
     * @return false면 백그라운드
     */
    fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        if (isNull(appProcesses)) {
            return false
        }
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 알림 허용 상태 값 가져오기
     */
    fun isNotificationsEnabled(context: Context?): Boolean {
        return NotificationManagerCompat.from(context!!).areNotificationsEnabled()
    }

    /**
     * 알림 허용 설정 메뉴로 이동
     */
    fun openNotificationSettingMenu(context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
        } else {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:" + context.packageName)
        }
        context.startActivity(intent)
    }

    /**
     * 만나이 18세 미만 체크
     *
     * @param birth  주민등록번호 앞번호 6자리
     * @param gender 뒷번호 1자리
     * @return
     */
    fun isAgeCheck(birth: String?, gender: String?): Boolean {
        var day: String? = null
        if ("1".equals(gender, ignoreCase = true) || "2".equals(gender, ignoreCase = true)) {
            day = StringBuilder("19").append(birth).toString()
        } else if ("3".equals(gender, ignoreCase = true) || "4".equals(gender, ignoreCase = true)) {
            day = StringBuilder("20").append(birth).toString()
        }
        val birthCalendar = getCalendarFromDateString(day)
        val currentCalendar = Calendar.getInstance()
        val birthYear = birthCalendar[Calendar.YEAR]
        val birthMonth = birthCalendar[Calendar.MONTH] + 1
        val birthDay = birthCalendar[Calendar.DAY_OF_MONTH]
        val currentYear = currentCalendar[Calendar.YEAR]
        val currentMonth = currentCalendar[Calendar.MONTH] + 1
        val currentDay = currentCalendar[Calendar.DAY_OF_MONTH]
        var age = currentYear - birthYear
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age--
        }
        return if (age < 17) false else true
    }

    /**
     * 날짜 String -> Calendar 변경 함수
     *
     * @param dateString yyyyMMdd 형식
     * @return Calendar
     */
    fun getCalendarFromDateString(dateString: String?): Calendar {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = dateString!!.substring(0, 4).toInt()
        cal[Calendar.MONTH] = dateString.substring(4, 6).toInt() - 1
        cal[Calendar.DATE] = dateString.substring(6, 8).toInt()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        return cal
    }

    /**
     * 생년월일 포맷 체크
     *
     * @param day 주민등록번호 앞6자리
     * @return
     */
    fun isBirthFormat(day: String): Boolean {
        val regExp = "^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|[3][0-1])"
        return day.matches(Regex(regExp))
    }

    /**
     * HTML 포맷 체크
     *
     * @param str 문자열
     * @return
     */
    fun isHtmlFormat(str: String): Boolean {
//        String regExp = "<([A-Za-z][A-Za-z0-9]*)\b[^>]*>(.*?)</\1>";
//        return str.matches(regExp);

        // 해당 문자열이 포함되어 있는지 체크. 임시 페이지의 index.html이 해당 문자열을 포함하고 있음.
        val checkString = "EMERGENCY_MESSAGE"
        return str.contains(checkString)
    }

    /**
     * 접근성 사용여부 체크
     *
     * @param context
     * @return
     */
    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (am != null && am.isEnabled) {
            val serviceInfoList =
                am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN)
            if (!serviceInfoList.isEmpty()) return true
        }
        return false
    }

    /**
     * Toast 출력
     *
     * @param context
     * @param resId   문자열ID
     */
    fun showToast(context: Context, resId: Int) {
        showToast(context, context.getString(resId))
    }

    /**
     * Toast 출력
     *
     * @param context
     * @param msg     문자열내용
     */
    fun showToast(context: Context?, msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 400)
        toast.show()
    }

    /**
     * onClick 시 클릭 연타를 방지해 주는 메소드
     *
     * @return 클릭 가능 여부
     */
    private var mPreviousBtnClickTime: Long = 0

    //웹 인터페이스 더블클릭 방지에서 더블클릭을 하지 아니하였지만 더블클릭으로 인식하여 다음 화면 안뜨는 문제.
    fun clearPreviousBtnClickTime() {
        mPreviousBtnClickTime = 0
    }

    // 동시 클릭 방지 (0.6초)
    val isEnableClickEvent: Boolean
        get() {
            // 동시 클릭 방지 (0.6초)
            if (SystemClock.uptimeMillis() - mPreviousBtnClickTime <= 600) return false
            mPreviousBtnClickTime = SystemClock.uptimeMillis()
            return true
        }

    fun changeFormatTelephoneNumber(phoneNum: String, dash: String): String {
        if (TextUtils.isEmpty(phoneNum)) return phoneNum
        var telNum = phoneNum
        telNum = if (phoneNum.length == 8) {
            phoneNum.replaceFirst("^([0-9]{4})([0-9]{4})$".toRegex(), "$1$dash$2")
        } else if (phoneNum.length == 12) {
            phoneNum.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$".toRegex(), "$1$dash$2$dash$3")
        } else {
            phoneNum.replaceFirst(
                "(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$".toRegex(),
                "$1$dash$2$dash$3"
            )
        }
        return telNum
    }

    /**
     * key가 존재하는지 체크
     *
     * @param json
     * @param key
     * @return
     */
    fun getJsonString(json: JSONObject, key: String?): String? {
        var outValue: String? = null
        if (json.has(key)) {
            try {
                outValue = json.getString(key)
            } catch (e: JSONException) {
                BLog.e(e)
            }
        }
        return outValue
    }

    /**
     * String Resource를 배열로 얻는다.
     *
     * @param resId
     * @return
     */
    fun getStringArray(resId: Array<String>): ArrayList<String> {
        val str = ArrayList<String>()
        for (cnt in resId.indices) {
            str.add(resId[cnt])
        }
        return str
    }

    /**
     *
     */
    fun addKeepScreenOn(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        BLog.d("add keep screen on")
    }

    fun clearKeepScreenOn(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        BLog.d("clear keep screen on")
    }
}