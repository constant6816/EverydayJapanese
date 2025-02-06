package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class AnnouncesResponseEntity {
    inner class AnnounceEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("url")
        val url: String? = null

        @SerializedName("createdDate")
        val createdDate: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<AnnounceEntity>? = null
}
