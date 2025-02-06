package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class RepliesResponseEntity {
    inner class ReplyEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("status")
        val status: String? = null

        @SerializedName("message")
        val message: String? = null

        @SerializedName("createdDate")
        val createdDate: String? = null

        @SerializedName("member")
        val member: MemberEntity? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<ReplyEntity>? = null

    @SerializedName("pageInfo")
    val pageInfo: PageInfoEntity? = null
}
