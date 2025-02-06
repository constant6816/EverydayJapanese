package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.AnnouncesResponseEntity
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class AnnounceModel {
    var id: Int
    var url: String
    var createdDate: LocalDateTime

    constructor(announceEntity: AnnouncesResponseEntity.AnnounceEntity) {
        this.id = nonNull(announceEntity.id)
        this.url = nonNull(announceEntity.url)
        this.createdDate = nonNull(announceEntity.createdDate).toLocalDateTime()
    }
}
