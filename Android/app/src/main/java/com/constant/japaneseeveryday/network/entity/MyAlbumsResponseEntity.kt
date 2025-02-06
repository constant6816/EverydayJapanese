package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MyAlbumsResponseEntity {
    inner class MyAlbumEntity {
        @SerializedName("id")
        val id: Int? = null

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

        @SerializedName("likeOn")
        val likeOn: Boolean? = null

        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("files")
        val files: Array<FileEntity>? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<MyAlbumEntity>? = null

    @SerializedName("cursorInfo")
    val cursorInfo: CursorInfoEntity? = null
}
