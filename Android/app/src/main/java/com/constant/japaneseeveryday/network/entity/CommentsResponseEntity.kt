package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class CommentsResponseEntity {
    inner class CommentEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("status")
        val status: String? = null

        @SerializedName("message")
        val message: String? = null

        @SerializedName("replyCount")
        val replyCount: Int? = null

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
    val responseData: Array<CommentEntity>? = null

    @SerializedName("pageInfo")
    val pageInfo: PageInfoEntity? = null
}
