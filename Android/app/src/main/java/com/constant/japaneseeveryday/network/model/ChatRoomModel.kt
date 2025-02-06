package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.ChatRoomsResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class ChatRoomModel {
    val chatRoomId: Int
    val members: ArrayList<MemberModel>
    val message: ChatMessageModel

    constructor(chatRoomId: Int, members: ArrayList<MemberModel>, message: ChatMessageModel) {
        this.chatRoomId = chatRoomId
        this.members = members
        this.message = message
    }

    constructor(chatRoomEntity: ChatRoomsResponseEntity.ChatRoomEntity?) {
        this.chatRoomId = nonNull(chatRoomEntity?.chatRoomId)
        this.members = ArrayList<MemberModel>()
        nonNull(chatRoomEntity?.members).forEach { element ->
            this.members.add(MemberModel(element))
        }
        this.message = ChatMessageModel(chatRoomEntity?.message)
    }

    override fun toString(): String {
        return "ChatRoomModel(chatRoomId=$chatRoomId, members=$members, message=$message)"
    }
}
