package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ChatRoomMessagesResponseEntity {
    inner class ChatRoomMessagesEntity {
        @SerializedName("chatRoomId")
        val chatRoomId: Int? = null

        @SerializedName("opponents")
        val opponents: Array<MemberEntity>? = null

        @SerializedName("messages")
        val messages: Array<ChatMessageEntity>? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: ChatRoomMessagesEntity? = null

    @SerializedName("cursorInfo")
    val cursorInfo: CursorInfoEntity? = null
}
