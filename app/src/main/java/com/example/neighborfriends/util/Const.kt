package com.example.neighborfriends.util

/**
 * Const : 공통으로 사용하는 상수를 정의
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */

object Const {
    /** 공통  */
    const val HTTP = "http://"
    const val HTTPS = "https://"
    const val MARKET = "market://"
    const val UTF_8 = "UTF-8"
    const val EUC_KR = "EUC-KR"
    const val SHA_256 = "SHA-256"
    const val GET = "GET"
    const val POST = "POST"
    const val DOT = "."
    const val COMMA = ","
    const val EMPTY = ""
    const val SLASH = "/"
    const val UNDER = "_"
    const val DASH = "-"
    const val EQUAL = "="
    const val AT = "@"
    const val AND = "&"
    const val ZERO = "0"
    const val ELLIPSIS = "∙∙∙"

    /** 메인 웹뷰 인텐트  */
    const val INTENT_MAINWEB_URL = "url"

    /** Was 요청 파리미터값  */
    const val REQUEST_WAS_YES = "Y"
    const val REQUEST_WAS_NO = "N"

    /** java script 기본 결과값  */
    const val BRIDGE_RESULT_KEY = "result"
    const val BRIDGE_RESULT_TRUE = "true"
    const val BRIDGE_RESULT_FALSE = "false"
    const val BRIDGE_RESULT_YES = "Y"
    const val BRIDGE_RESULT_NO = "N"

    /** 코드 조회 결과값 공통  */
    const val REQUEST_COMMON_HEAD = "COMMON_HEAD"
    const val REQUEST_COMMON_ERROR = "ERROR"
    const val REQUEST_COMMON_MESSAGE = "MESSAGE"
    const val REQUEST_COMMON_CODE = "CODE"

    /** 로그아웃 인텐트  */
    const val INTENT_LOGOUT_TYPE = "intent_logout_type"

    /** 로그아웃 타입  */
    const val LOGOUT_TYPE_USER = 0
    const val LOGOUT_TYPE_TIMEOUT = 1
    const val LOGOUT_TYPE_FORCE = 2

    /** 주소록 검색 결과 인텐트 */
    const val INTENT_RESULT_ZIPCODE = "intent_result_zipcode" //우편번호
    const val INTENT_RESULT_BLDB_CODE = "intent_result_bldg_code" //빌딩코드
    const val INTENT_RESULT_ADDRESS = "intent_result_address" //소재지주소
    const val INTENT_RESULT_ADDRESS_DETAIL = "intent_result_address_detail" //상세주소

    /** 퍼미션 요청코드  */
    const val REQUEST_PERMISSION_READ_PHONE_STATE = 1001
    const val REQUEST_PERMISSION_CALL = 1002
    const val REQUEST_PERMISSION_CAMERA = 1003
    const val REQUEST_PERMISSION_EXTERNAL_STORAGE = 1004
    const val REQUEST_PERMISSION_SMS = 1005
    const val REQUEST_PERMISSION_READ_CONTACTS = 1006
    const val REQUEST_PERMISSION_SYNC_CONTACTS = 1007
    const val REQUEST_PERMISSION_READ_PHONE_NUMBERS = 1008

    /** 디버깅 서버 선택  */
    const val DEBUGING_SERVER_NONE = -1
    const val DEBUGING_SERVER_DEV = 0
    const val DEBUGING_SERVER_TEST = 1
    const val DEBUGING_SERVER_OPERATION = 2

    /** 입력창 한글 유효성  */
    const val INDEX_EDITBOX_NAME = 0
    const val INDEX_EDITBOX_BIRTH = 1
    const val INDEX_EDITBOX_EMAIL = 2
    const val STATE_EDITBOX_NORMAL = 0
    const val STATE_EDITBOX_FOCUSED = 1
    const val STATE_EDITBOX_ERROR = 2
    const val STATE_VALID_STR_NORMAL = 0
    const val STATE_VALID_STR_LENGTH_ERR = 1
    const val STATE_VALID_STR_CHAR_ERR = 2
}