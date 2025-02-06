package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ChatMessageEntity {
    @SerializedName("member")
    val member: MemberEntity? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("createdDate")
    val createdDate: String? = null
}
