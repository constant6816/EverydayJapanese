package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class AlbumsResponseEntity {
    inner class AlbumEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("member")
        val member: MemberEntity? = null

        @SerializedName("mimeType")
        val mimeType: String? = null

        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String? = null

        @SerializedName("rawUrl")
        val rawUrl: String? = null

        @SerializedName("originalFileName")
        val originalFileName: String? = null

        @SerializedName("durationTime")
        val durationTime: Double? = null

        @SerializedName("createdDate")
        val createdDate: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<AlbumEntity>? = null

    @SerializedName("cursorInfo")
    val cursorInfo: CursorInfoEntity? = null
}
