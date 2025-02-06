package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class HasBannerResponseEntity {
    inner class HasBannerEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("imageUrl")
        val imageUrl: String? = null

        @SerializedName("linkUrl")
        val linkUrl: String? = null

        @SerializedName("isVisible")
        val isVisible: Boolean? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: HasBannerEntity? = null
}
