package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class LoginResponseEntity {
    inner class LoginEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("email")
        val email: String? = null

        @SerializedName("accessToken")
        val accessToken: String? = null

        @SerializedName("refreshToken")
        val refreshToken: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: LoginEntity? = null
}
