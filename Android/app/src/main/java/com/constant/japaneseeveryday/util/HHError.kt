package com.constant.japaneseeveryday.util

import org.json.JSONObject
import retrofit2.Response

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
enum class HHResponseCode(val value: Int) {
    OK(200),
    UNAUTHORIZED(401),
    NOTACCEPTABLE(406),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                200 to OK,
                401 to UNAUTHORIZED,
                406 to NOTACCEPTABLE,
            )

        fun ofRaw(raw: Int): HHResponseCode {
            return rawToEnum[raw] ?: NOTACCEPTABLE
        }
    }
}

enum class HHError(val value: String) {
    NONE("NONE"),
    NO_RESPONSE("NO_RESPONSE"), // 서버로 부터 응답이 없을때
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND"), // 회원이 아닐때
    EXPIRED_ACCESS_TOKEN("EXPIRED_ACCESS_TOKEN"), // 토큰 만료
    EXPIRED_REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN"), // RefreshToken을 가지고 reissue를 했는데, Refresh Token 이 만료 되었을때 (1주일 정도 장기간 아무 동작 없을때 발생함)
    BAN_USER("BAN_USER"),
    ALREADY_REGISTER_CLUB("ALREADY_REGISTER_CLUB"),
    KICKED_OUT_CLUB("KICKED_OUT_CLUB"),
    CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB("CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB"),
    ALREADY_REQUESTING("ALREADY_REQUESTING"),
    OTHERS("OTHERS"), // 나머지
    ;

    companion object {
        val rawToEnum =
            mapOf(
                "NONE" to HHError.NONE,
                "NO_RESPONSE" to HHError.NO_RESPONSE,
                "MEMBER_NOT_FOUND" to HHError.MEMBER_NOT_FOUND,
                "EXPIRED_ACCESS_TOKEN" to HHError.EXPIRED_ACCESS_TOKEN,
                "EXPIRED_REFRESH_TOKEN" to HHError.EXPIRED_REFRESH_TOKEN,
                "BAN_USER" to HHError.BAN_USER,
                "ALREADY_REGISTER_CLUB" to HHError.ALREADY_REGISTER_CLUB,
                "KICKED_OUT_CLUB" to HHError.KICKED_OUT_CLUB,
                "CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB" to HHError.CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB,
                "ALREADY_REQUESTING" to HHError.ALREADY_REQUESTING,
                "OTHERS" to HHError.OTHERS,
            )

        fun ofRaw(raw: String): HHError {
            return HHError.rawToEnum[raw] ?: HHError.NONE
        }
    }
}

// 406번 에러에서의 세부에러
object HHSubError {
    // Login 관련
    val MEMBER_NOT_FOUND = 40001

    // Security 관련 에러
    val REFRESH_TOKEN_EXPIRED = 40100

    // Rest API 형식 에러
    val ILLEGAL_HEADER = 40200
    val ILLEGAL_BODY = 40201

    // Board 관련 에러
    val NO_PERMISSION = 40300

    // Chatting 관련 에러
    val BAN_USER = 40400

    // Club 관련 에러
    val ALREADY_REGISTER_CLUB = 40500
    val KICKED_OUT_CLUB = 40501
    val CANNOT_WITHDRAWAL_CLUB = 40510
    val CANNOT_REMOVE_CLUB = 40511
    val CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB = 40512
    val ALREADY_REQUESTING = 40513
    val CANNOT_APPROVE = 40514

    // 근태 관련 에러
    val OVER_MAX_WORK_MINUTES = 40600
    val UNDER_MIN_WORK_MINUTES = 40601
    val CANNOT_CANCEL_ATTENDANCE = 40602
    val CANNOT_APPROVE_ATTENDANCE = 40603

    // 일반 에러 관련
    val LOCATION_ERROR = 41000

    // ETC
    val UNKNOWN_ERROR = 42000
}

// ----------------------------------------------------
// Public Method
fun <T> getHHError(response: Response<T>): HHError {
    if (response.isSuccessful) {
        return HHError.NONE
    } else {
        when (response.code()) {
            HHResponseCode.UNAUTHORIZED.value -> {
                return HHError.EXPIRED_ACCESS_TOKEN
            }
            HHResponseCode.NOTACCEPTABLE.value -> {
                val jObjError = JSONObject(response.errorBody()!!.string())
                val responseCode = jObjError.getInt("responseCode")
                if (responseCode == HHSubError.MEMBER_NOT_FOUND) {
                    return HHError.MEMBER_NOT_FOUND
                } else if (responseCode == HHSubError.BAN_USER) {
                    return HHError.BAN_USER
                } else if (responseCode == HHSubError.ALREADY_REGISTER_CLUB) {
                    return HHError.ALREADY_REGISTER_CLUB
                } else if (responseCode == HHSubError.KICKED_OUT_CLUB) {
                    return HHError.KICKED_OUT_CLUB
                } else if (responseCode == HHSubError.CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB) {
                    return HHError.CANNOT_REGISTER_PREVIOUS_REGISTER_CLUB
                } else if (responseCode == HHSubError.ALREADY_REQUESTING) {
                    return HHError.ALREADY_REQUESTING
                } else {
                    return HHError.OTHERS
                }
            }
        }
    }
    return HHError.OTHERS
}
