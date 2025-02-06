package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class PageInfoEntity {
    @SerializedName("totalPages")
    val totalPages: Int? = null

    @SerializedName("currentPage")
    val currentPage: Int? = null

    @SerializedName("totalRows")
    val totalRows: Int? = null

    @SerializedName("pageSize")
    val pageSize: Int? = null
}
