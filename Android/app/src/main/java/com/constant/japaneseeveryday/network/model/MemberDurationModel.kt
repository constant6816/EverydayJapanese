package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.MemberDurationsResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class MemberDurationModel {
    val member: MemberModel
    val seconds: Int

    constructor(memberDurationEntity: MemberDurationsResponseEntity.MemberDurationEntity) {
        this.member = MemberModel(memberDurationEntity.member)
        this.seconds = nonNull(memberDurationEntity.seconds)
    }
}
