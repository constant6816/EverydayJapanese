package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class AttendanceEntity {
    @SerializedName("startTime")
    val startTime: String? = null

    @SerializedName("endTime")
    val endTime: String? = null

    @SerializedName("attendance")
    val attendance: String? = null
}
