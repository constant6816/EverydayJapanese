package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class DailyDurationsResponseEntity {
    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<DailyDurationEntity>? = null
}

class DailyDurationEntity {
    @SerializedName("date")
    val date: String? = null

    @SerializedName("seconds")
    val seconds: Int? = null
}
