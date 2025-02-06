package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ClubsResponseEntity {
    inner class ClubEntity {
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
        val latitude: Double? = null

        @SerializedName("longitude")
        val longitude: Double? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: Array<ClubEntity>? = null

    @SerializedName("pageInfo")
    val pageInfo: PageInfoEntity? = null
}
