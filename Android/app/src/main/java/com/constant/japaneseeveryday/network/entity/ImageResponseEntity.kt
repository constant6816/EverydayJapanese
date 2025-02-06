package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ImageResponseEntity {
    inner class ImageEntity {
        @SerializedName("fileUrl")
        val fileUrl: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: ImageEntity? = null
}
