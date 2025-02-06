package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.MyAlbumsResponseEntity
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class MyAlbumsModel {
    inner class MyAlbumModel {
        val id: Int
        val content: String
        var createdDate: LocalDateTime
        val hit: Int
        val commentCount: Int
        var likeCount: Int
        var likeOn: Boolean
        val member: MemberModel
        val files: ArrayList<FileModel>

        constructor(myAlbumEntity: MyAlbumsResponseEntity.MyAlbumEntity) {
            this.id = nonNull(myAlbumEntity.id)
            this.content = nonNull(myAlbumEntity.content)
            this.createdDate = nonNull(myAlbumEntity.createdDate).toLocalDateTime()
            this.hit = nonNull(myAlbumEntity.hit)
            this.commentCount = nonNull(myAlbumEntity.commentCount)
            this.likeCount = nonNull(myAlbumEntity.likeCount)
            this.likeOn = nonNull(myAlbumEntity.likeOn)
            this.member = MemberModel(myAlbumEntity.member)
            files = ArrayList<FileModel>()
            myAlbumEntity.files?.let {
                it.forEach { fileEntity ->
                    files.add(FileModel(fileEntity))
                }
            }
        }
    }

    val myAlbums: ArrayList<MyAlbumModel>
    val cursorInfo: CursorInfoModel

    constructor(myAlbumsResponseEntity: MyAlbumsResponseEntity?) {
        myAlbums = ArrayList<MyAlbumModel>()
        myAlbumsResponseEntity?.responseData?.let {
            it.forEach { myAlbumEntity ->
                myAlbums.add(MyAlbumModel(myAlbumEntity))
            }
        }
        cursorInfo = CursorInfoModel(myAlbumsResponseEntity?.cursorInfo)
    }
}
