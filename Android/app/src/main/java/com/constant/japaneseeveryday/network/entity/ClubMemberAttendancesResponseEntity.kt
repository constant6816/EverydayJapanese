package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MemberAttendanceEntity {
    @SerializedName("member")
    val member: MemberEntity? = null

    @SerializedName("startTime")
    val startTime: String? = null

    @SerializedName("endTime")
    val endTime: String? = null

    @SerializedName("attendance")
    val attendance: String? = null
}

class ClubMemberAttendancesResponseEntity {
    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<MemberAttendanceEntity>? = null
}
