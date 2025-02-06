package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ClubChatMessagesResponseEntity {
    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<ChatMessageEntity>? = null

    @SerializedName("cursorInfo")
    val cursorInfo: CursorInfoEntity? = null
}
