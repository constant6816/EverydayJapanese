package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.BoardsResponseEntity
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class BoardsModel {
    inner class BoardModel {
        val id: Int
        var title: String
        var area: String
        val content: String
        var createdDate: LocalDateTime
        val hit: Int
        var commentCount: Int
        var likeCount: Int
        val member: MemberModel
        val thumbnailUrl: String?
        val fileCount: Int

        constructor(boardEntity: BoardsResponseEntity.BoardEntity) {
            this.id = nonNull(boardEntity.id)
            this.title = nonNull(boardEntity.title)
            this.area = nonNull(boardEntity.area)
            this.content = nonNull(boardEntity.content)
            this.createdDate = nonNull(boardEntity.createdDate).toLocalDateTime()
            this.hit = nonNull(boardEntity.hit)
            this.commentCount = nonNull(boardEntity.commentCount)
            this.likeCount = nonNull(boardEntity.likeCount)
            this.member = MemberModel(boardEntity.member)
            this.thumbnailUrl = boardEntity.thumbnailUrl
            this.fileCount = nonNull(boardEntity.fileCount)
        }
    }

    val boards: ArrayList<BoardModel>
    val cursorInfo: CursorInfoModel

    constructor(boardsResponseEntity: BoardsResponseEntity?) {
        boards = ArrayList<BoardModel>()
        boardsResponseEntity?.responseData?.let {
            it.forEach { boardEntity ->
                boards.add(BoardModel(boardEntity))
            }
        }
        cursorInfo = CursorInfoModel(boardsResponseEntity?.cursorInfo)
    }
}
