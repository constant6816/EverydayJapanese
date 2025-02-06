package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.ChatMessageEntity
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class ChatMessageModel {
    val member: MemberModel
    val message: String
    val createdDate: LocalDateTime

    constructor(chatMessageEntity: ChatMessageEntity?) {
        this.member = MemberModel(chatMessageEntity?.member)
        this.message = nonNull(chatMessageEntity?.message)
        this.createdDate = nonNull(chatMessageEntity?.createdDate).toLocalDateTime()
    }

    constructor(member: MemberModel, message: String, createdDate: LocalDateTime) {
        this.member = member
        this.message = message
        this.createdDate = createdDate
    }
}
