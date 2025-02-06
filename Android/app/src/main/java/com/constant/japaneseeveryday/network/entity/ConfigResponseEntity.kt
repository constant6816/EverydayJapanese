package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ConfigResponseEntity {
    inner class ConfigEntity {
        @SerializedName("appName")
        val appName: String? = null

        @SerializedName("appServerName")
        val appServerName: String? = null

        @SerializedName("marketVersion")
        val marketVersion: String? = null

        @SerializedName("isRequiredUpdate")
        val isRequiredUpdate: Boolean? = null

        @SerializedName("minWorkMinute")
        val minWorkMinute: Int? = null

        @SerializedName("maxWorkMinute")
        val maxWorkMinute: Int? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: ConfigEntity? = null
}
