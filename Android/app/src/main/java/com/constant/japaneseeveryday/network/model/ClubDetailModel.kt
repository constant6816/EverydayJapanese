package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.ClubDetailResponseEntity
import com.constant.japaneseeveryday.util.AccessLevelEnum
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.PositionEnum
import com.constant.japaneseeveryday.util.nonNull

class ClubDetailModel {
    val id: Int
    val name: String
    val accessLevel: AccessLevelEnum
    val latitude: Double
    val longitude: Double
    val province: String
    val city: String
    val area: String
    val description: String
    val maxNumPeople: Int
    val image: FileModel
    val position: PositionEnum
    val members: ArrayList<MemberModel>

    constructor(clubDetailResponseEntity: ClubDetailResponseEntity) {
        this.id = nonNull(clubDetailResponseEntity.responseData?.id)
        this.name = nonNull(clubDetailResponseEntity.responseData?.name)
        this.accessLevel = AccessLevelEnum.ofRaw(nonNull(clubDetailResponseEntity.responseData?.accessLevel))
        this.latitude = nonNull(clubDetailResponseEntity.responseData?.latitude).toDouble()
        this.longitude = nonNull(clubDetailResponseEntity.responseData?.longitude).toDouble()
        this.province = nonNull(clubDetailResponseEntity.responseData?.province)
        this.city = nonNull(clubDetailResponseEntity.responseData?.city)
        this.area = nonNull(clubDetailResponseEntity.responseData?.area)
        this.description = nonNull(clubDetailResponseEntity.responseData?.description)
        this.maxNumPeople = nonNull(clubDetailResponseEntity.responseData?.maxNumPeople)
        this.image = FileModel(clubDetailResponseEntity.responseData?.image)
        this.position = PositionEnum.ofRaw(nonNull(clubDetailResponseEntity.responseData?.position))
        members = ArrayList<MemberModel>()
        clubDetailResponseEntity?.responseData?.members.let {
            nonNull(it).forEach { memberEntity ->
                members.add(MemberModel(memberEntity))
            }
        }
    }

    constructor() {
        this.id = 0
        this.name = ""
        this.accessLevel = AccessLevelEnum.private
        this.latitude = 0.0
        this.longitude = 0.0
        this.province = ""
        this.city = ""
        this.area = ""
        this.description = ""
        this.maxNumPeople = 0
        this.image = FileModel(0, "", MimeType.image, "", "", "", 0.0)
        this.position = PositionEnum.notMember
        this.members = ArrayList()
    }
}
