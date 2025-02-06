package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MemberEntity {
    @SerializedName("id")
    val id: Int? = null

    @SerializedName("nickname")
    val nickname: String? = null

    @SerializedName("introduction")
    val introduction: String? = null

    @SerializedName("status")
    val status: String? = null

    @SerializedName("email")
    val email: String? = null

    @SerializedName("profileUrl")
    val profileUrl: String? = null

    @SerializedName("role")
    val role: String? = null

    @SerializedName("isEditable")
    val isEditable: Boolean? = null

    @SerializedName("isMe")
    val isMe: Boolean? = null

    @SerializedName("position")
    val position: String? = null
}
