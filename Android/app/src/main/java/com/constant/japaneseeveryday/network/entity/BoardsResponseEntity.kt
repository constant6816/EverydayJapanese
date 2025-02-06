package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class BoardsResponseEntity {
    inner class BoardEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("title")
        val title: String? = null

        @SerializedName("area")
        val area: String? = null

        @SerializedName("content")
        val content: String? = null

        @SerializedName("createdDate")
        val createdDate: String? = null

        @SerializedName("hit")
        val hit: Int? = null

        @SerializedName("commentCount")
        val commentCount: Int? = null

        @SerializedName("likeCount")
        val likeCount: Int? = null

        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String? = null

        @SerializedName("fileCount")
        val fileCount: Int? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<BoardEntity>? = null

    @SerializedName("cursorInfo")
    val cursorInfo: CursorInfoEntity? = null
}
