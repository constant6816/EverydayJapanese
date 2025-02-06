package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class AddressResponseEntity {
    inner class MetaEntity {
        @SerializedName("total_count")
        val total_count: Int? = null
    }

    inner class AddressEntity {
        @SerializedName("region_type")
        val region_type: String? = null

        @SerializedName("code")
        val code: String? = null

        @SerializedName("address_name")
        val address_name: String? = null

        @SerializedName("region_1depth_name")
        val region_1depth_name: String? = null

        @SerializedName("region_2depth_name")
        val region_2depth_name: String? = null

        @SerializedName("region_3depth_name")
        val region_3depth_name: String? = null

        @SerializedName("region_4depth_name")
        val region_4depth_name: String? = null

        @SerializedName("x")
        val x: Double? = null

        @SerializedName("y")
        val y: Double? = null
    }

    @SerializedName("meta")
    val meta: MetaEntity? = null

    @SerializedName("documents")
    val documents: Array<AddressEntity>? = null
}
