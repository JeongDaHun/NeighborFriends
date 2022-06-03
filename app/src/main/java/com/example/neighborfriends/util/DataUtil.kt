package com.example.neighborfriends.util

import kotlin.jvm.JvmOverloads
import android.os.Parcel
import android.os.Bundle
import java.lang.Exception
import java.lang.RuntimeException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.HashMap

/**
 * DataUtil : 데이터 관련 유틸
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */
object DataUtil {
    /**
     * 주어진 스트링이 null 이거나 nullstring(사이즈=0) 인지 체크한다.
     *
     * @param str
     * @return
     */
    fun isNull(str: Any?): Boolean {
        return isNull(str, false)
    }

    fun isNotNull(str: Any?): Boolean {
        return !isNull(str, false)
    }

    /**
     * 주어진 스트링이 null 이거나 nullstring(사이즈=0) 인지 체크한다.
     *
     * @param str
     * @param checkTrim 좌우여백 제거하고 체크할지 여부
     * @return
     */
    fun isNull(str: Any?, checkTrim: Boolean): Boolean {
        return str == null || (if (checkTrim) str.toString()
            .trim { it <= ' ' }.length else str.toString().length) == 0
    }

    /**
     * 주어진 스트링이 null 이면 defaultStr 를 반환하고, 다른 경우엔 그냥 str 그대로 반환
     *
     * @param str
     * @param defaultStr
     * @return
     */
    @JvmOverloads
    fun checkNull(str: String?, defaultStr: String = ""): String {
        return str ?: defaultStr
    }

    /**
     * 주어진 스트링이 null 또는 "null" str 이면 defaultStr 를 반환하고, 다른 경우엔 그냥 str 그대로 반환
     *
     * @param str
     * @param defaultStr
     * @return
     */
    fun checkNullStr(str: String?, defaultStr: String): String {
        return if (str == null) defaultStr else if ("null" == str.lowercase()) defaultStr else str
    }

    /**
     * 주어진 스트링이 "null" 과 같은지 체크한다.
     *
     * @param str
     * @return
     */
    fun isNullStr(str: Any): Boolean {
        return "null" == str
    }
    /**
     * 주어진 문자열을 숫자로 반환한다. 파싱이 실패할 경우 defaultValue 를 반환한다.
     *
     * @param str
     * @param defaultValue
     * @return
     */
    /**
     * 주어진 문자열을 반환한다. 파싱이 실패한 경우 -1 을 반환한다.
     *
     * @param str
     * @return
     */
    @JvmOverloads
    fun parseInt(str: String, defaultValue: Int = -1): Int {
        return try {
            str.toInt()
        } catch (ex: RuntimeException) {
            defaultValue
        }
    }
    /**
     * 주어진 숫자를 문자열로 변환한다. 파싱이 실패할 경우 defaultValue 를 반환한다.
     *
     * @param i
     * @param defaultValue
     * @return
     */
    /**
     * 주어진 숫자를 문자열로 변환한다.
     *
     * @param i
     * @return
     */
    @JvmOverloads
    fun parseString(i: Int, defaultValue: String = ""): String {
        return try {
            i.toString()
        } catch (ex: RuntimeException) {
            defaultValue
        }
    }

    /**
     * decode
     *
     * @param value
     * @return
     */
    fun decode(value: String?): String? {
        var value = value ?: return null
        value = value.replace("&#x27;", "'")
        value = value.replace("&amp;", "&")
        value = value.replace("&quot;", "\"")
        value = value.replace("&lt;", "<")
        value = value.replace("&gt;", ">")
        value = value.replace("&#x2F;", "/")
        return value
    }

    fun encode(value: String?): String? {
        var value = value ?: return null
        value = value.replace("'", "&#x27;")
        value = value.replace("&", "&amp;")
        value = value.replace("\"", "&quot;")
        value = value.replace("<", "&lt;")
        value = value.replace(">", "&gt;")
        value = value.replace("/", "&#x2F;")
        return value
    }

    @JvmOverloads
    fun urldecode(src: String?, encoding: String? = "utf-8"): String? {
        return try {
            URLDecoder.decode(src, encoding)
        } catch (ex: Exception) {
            BLog.e("DataUtil.urlencode()", ex)
            null
        }
    }

    @JvmOverloads
    fun urlencode(src: String?, encoding: String? = "utf-8"): String? {
        return try {
            URLEncoder.encode(src, encoding)
        } catch (ex: Exception) {
            BLog.e("DataUtil.urlencode()", ex)
            null
        }
    }

    /**
     * 숫자를 천단위마다 ','로 끊어서 스트링으로 반환 1234 --> 1,234
     *
     * @param num
     * @return
     */
    fun toCommaNumber(num: Int): String {
        return if (num < 1000) {
            Integer.toString(num)
        } else {
            val tail = num % 1000
            var tailStr = Integer.toString(tail)
            if (tail < 10) tailStr = leftPadding(tailStr, "0", 2) else if (tail < 99) tailStr =
                leftPadding(tailStr, "0", 1)
            toCommaNumber(((num - tail) / 1000)) + "," + tailStr
        }
    }

    /**
     * String 앞에 주어진 String을 count 번 덧붙인다.
     *
     * @param src
     * @param c
     * @param count
     * @return
     */
    fun leftPadding(src: String?, c: String?, count: Int): String {
        val sb = StringBuffer()
        for (i in 0 until count) {
            sb.append(c)
        }
        sb.append(src)
        return sb.toString()
    }

    fun trim(body: String?): String? {
        return body?.trim { it <= ' ' }
    }

    fun writeStrMapToParcelable(map: HashMap<String, String?>?, out: Parcel) {
        if (map != null && map.size > 0) {
            val keySet: Set<String> = map.keys
            val b = Bundle()
            for (key in keySet) {
                b.putString(key, map[key])
            }
            val array = keySet.toTypedArray()
            out.writeStringArray(array)
            out.writeBundle(b)
        } else {
            out.writeStringArray(arrayOfNulls(0))
            out.writeBundle(Bundle.EMPTY)
        }
    }

    fun readStrMapFromParcelable(`in`: Parcel?): HashMap<String, String?> {
        val map = HashMap<String, String?>()
        if (`in` != null) {
            val keys = `in`.createStringArray()
            val bundle = `in`.readBundle()
            for (key in keys!!) {
                map[key] = bundle!!.getString(key)
            }
        }
        return map
    }

    fun getUrlKeyValue(url: String?, key: String?): String? {
        var value: String? = null
        var refUrl: String? = null
        if (url == null) return null
        val startIdx = url.indexOf("?")
        refUrl = url.substring(startIdx + 1)
        val srcData = refUrl.split("[&|#]").toTypedArray()
        var resultData: Array<String?>
        for (data in srcData) {
            if (data.trim { it <= ' ' }.indexOf(key!!) == 0) {
                resultData = data.trim { it <= ' ' }.split("=").toTypedArray()
                if (resultData.size > 1) {
                    value = resultData[1]
                }
            }
        }
        return value
    }

    /**
     * 특수문자 입력 체크
     *
     * @param str
     * @return
     */
    fun checkString(str: String): Boolean {
        val checkChar = charArrayOf(
            '.',
            '^',
            '$',
            '-',
            '+',
            '*',
            '=',
            '<',
            '>',
            '\\',
            '/',
            '[',
            ']',
            '%',
            '~',
            '!',
            '#',
            '&',
            '(',
            ')',
            '_',
            '\'',
            '"'
        )
        var bFlag = false
        val arr = str.toCharArray()
        for (i in arr.indices) {
            for (k in checkChar.indices) {
                if (arr[i] == checkChar[k]) {
                    bFlag = true
                    break
                }
            }
        }
        return bFlag
    }
}