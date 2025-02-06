package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ClubDetailResponseEntity {
    inner class ClubDetailEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("accessLevel")
        val accessLevel: String? = null

        @SerializedName("latitude")
        val latitude: String? = null

        @SerializedName("longitude")
        val longitude: String? = null

        @SerializedName("province")
        val province: String? = null

        @SerializedName("city")
        val city: String? = null

        @SerializedName("area")
        val area: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("maxNumPeople")
        val maxNumPeople: Int? = null

        @SerializedName("image")
        val image: FileEntity? = null

        @SerializedName("position")
        val position: String? = null

        @SerializedName("members")
        val members: Array<MemberEntity>? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: ClubDetailEntity? = null
}
