package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ChatRoomsResponseEntity {
    inner class ChatRoomEntity {
        @SerializedName("chatRoomId")
        val chatRoomId: Int? = null

        @SerializedName("members")
        val members: Array<MemberEntity>? = null

        @SerializedName("message")
        val message: ChatMessageEntity? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<ChatRoomEntity>? = null
}
