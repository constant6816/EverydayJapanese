package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class CursorInfoEntity {
    @SerializedName("cursor")
    val cursor: Int? = null

    @SerializedName("hasNext")
    val hasNext: Boolean? = null
}
