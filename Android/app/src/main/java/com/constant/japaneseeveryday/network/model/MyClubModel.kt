package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toOptionalLocalDateTime
import com.constant.japaneseeveryday.network.entity.MyClubsResponseEntity
import com.constant.japaneseeveryday.util.StatusEnum
import com.constant.japaneseeveryday.util.nonNull
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

class MyClubModel {
    val id: Int
    val name: String
    val description: String
    val maxNumPeople: Int
    val thumbnailUrl: String

    val province: String
    val city: String
    val area: String

    val latitude: Double
    val longitude: Double

    var canCheckout: Boolean
    var startTime: LocalDateTime?

    val status: StatusEnum

    val memberCount: Int
    val boardCount: Int
    val albumCount: Int

    constructor(myClubEntity: MyClubsResponseEntity.MyClubEntity) {
        this.id = nonNull(myClubEntity.id)
        this.name = nonNull(myClubEntity.name)
        this.description = nonNull(myClubEntity.description)
        this.maxNumPeople = nonNull(myClubEntity.maxNumPeople)
        this.thumbnailUrl = nonNull(myClubEntity.thumbnailUrl)

        this.province = nonNull(myClubEntity.province)
        this.city = nonNull(myClubEntity.city)
        this.area = nonNull(myClubEntity.area)

        this.latitude = nonNull(myClubEntity.latitude).toDouble()
        this.longitude = nonNull(myClubEntity.longitude).toDouble()

        this.canCheckout = nonNull(myClubEntity.canCheckout)
        this.startTime = nonNull(myClubEntity.startTime).toOptionalLocalDateTime()

        this.status = StatusEnum.ofRaw(nonNull(myClubEntity.status))

        this.memberCount = nonNull(myClubEntity.memberCount)
        this.boardCount = nonNull(myClubEntity.boardCount)
        this.albumCount = nonNull(myClubEntity.albumCount)
    }
}
