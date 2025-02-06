package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MemberDailyDurationsResponseEntity {
    inner class MemberDailyDurationEntity {
        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("dailyDurations")
        val dailyDurations: List<DailyDurationEntity>? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<MemberDailyDurationEntity>? = null
}
