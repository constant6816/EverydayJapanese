package com.constant.japaneseeveryday.network.entity

import com.google.gson.annotations.SerializedName

class ApprovalsResponseEntity {
    inner class ApprovalEntity {
        @SerializedName("approvalType")
        val approvalType: String? = null

        @SerializedName("id")
        val id: Int? = null

        @SerializedName("startTime")
        val startTime: String? = null

        @SerializedName("endTime")
        val endTime: String? = null

        @SerializedName("requestTime")
        val requestTime: String? = null

        @SerializedName("memberName")
        val memberName: String? = null

        @SerializedName("clubName")
        val clubName: String? = null

        @SerializedName("attendance")
        val attendance: String? = null
    }

    @SerializedName("responseCode")
    val responseCode: Int? = null

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseData")
    val responseData: List<ApprovalEntity>? = null

    @SerializedName("pageInfo")
    val pageInfo: PageInfoEntity? = null
}
