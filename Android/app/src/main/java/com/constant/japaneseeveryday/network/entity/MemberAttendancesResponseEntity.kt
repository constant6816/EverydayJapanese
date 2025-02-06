package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MemberAttendancesResponseEntity {
    inner class MemberAttendancesEntity {
        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("attendances")
        val attendances: List<AttendanceEntity>? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: MemberAttendancesEntity? = null
}
