package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MemberDurationsResponseEntity {
    inner class MemberDurationEntity {
        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("seconds")
        val seconds: Int? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<MemberDurationEntity>? = null
}
