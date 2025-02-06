package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class FileEntity {
    @SerializedName("id")
    val id: Int? = null

    @SerializedName("fileName")
    val fileName: String? = null

    @SerializedName("type")
    val type: String? = null

    @SerializedName("normalUrl")
    val normalUrl: String? = null

    @SerializedName("rawUrl")
    val rawUrl: String? = null

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null

    @SerializedName("durationTime")
    val durationTime: Double? = null
}
