package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class OneReplyResponseEntity {
    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: RepliesResponseEntity.ReplyEntity? = null
}
