package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ReissueResponseEntity {
    inner class ReissueResponseDataEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("email")
        val email: String? = null

        @SerializedName("phoneNumber")
        val phoneNumber: String? = null

        @SerializedName("grantType")
        val grantType: String? = null

        @SerializedName("accessToken")
        val accessToken: String? = null

        @SerializedName("refreshToken")
        val refreshToken: String? = null

        @SerializedName("accessTokenExpiresIn")
        val accessTokenExpiresIn: Int? = null

        @SerializedName("refreshTokenExpiresIn")
        val refreshTokenExpiresIn: Int? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: ReissueResponseDataEntity? = null
}
