package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class MyClubsResponseEntity {
    inner class MyClubEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("maxNumPeople")
        val maxNumPeople: Int? = null

        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String? = null

        @SerializedName("province")
        val province: String? = null

        @SerializedName("city")
        val city: String? = null

        @SerializedName("area")
        val area: String? = null

        @SerializedName("latitude")
        val latitude: String? = null

        @SerializedName("longitude")
        val longitude: String? = null

        @SerializedName("canCheckout")
        val canCheckout: Boolean? = null

        @SerializedName("startTime")
        val startTime: String? = null

        @SerializedName("status")
        val status: String? = null

        @SerializedName("memberCount")
        val memberCount: Int? = null

        @SerializedName("boardCount")
        val boardCount: Int? = null

        @SerializedName("albumCount")
        val albumCount: Int? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<MyClubEntity>? = null

    @SerializedName("pageInfo")
    val pageInfo: PageInfoEntity? = null
}
