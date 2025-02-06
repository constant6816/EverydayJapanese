package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class PushNotificationsResponseEntity {
    inner class PushNotificationEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("title")
        val title: String? = null

        @SerializedName("body")
        val body: String? = null

        @SerializedName("linkUrl")
        val linkUrl: String? = null

        @SerializedName("isSuccessful")
        val isSuccessful: Boolean? = null

        @SerializedName("createdDate")
        val createdDate: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<PushNotificationEntity>? = null
}
