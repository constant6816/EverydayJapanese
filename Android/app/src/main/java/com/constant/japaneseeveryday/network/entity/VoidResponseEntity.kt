package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class VoidResponseEntity {
    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null
}
