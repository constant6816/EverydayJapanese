package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class LikeResponseEntity {
    inner class LikeEntity {
        @SerializedName("likeCount")
        val likeCount: Int? = null

        @SerializedName("likeOn")
        val likeOn: Boolean? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: LikeEntity? = null
}
