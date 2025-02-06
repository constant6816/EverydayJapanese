package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.AlbumsResponseEntity
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class AlbumsModel {
    inner class AlbumModel {
        val id: Int
        val member: MemberModel
        var mimeType: MimeType
        var thumbnailUrl: String
        var rawUrl: String
        val originalFileName: String
        var durationTime: Double
        var createdDate: LocalDateTime

        constructor(albumEntity: AlbumsResponseEntity.AlbumEntity) {
            this.id = nonNull(albumEntity.id)
            this.member = MemberModel(albumEntity.member)
            when (nonNull(albumEntity.mimeType)) {
                MimeType.image.getShortString() -> {
                    this.mimeType = MimeType.image
                }
                MimeType.video.getShortString() -> {
                    this.mimeType = MimeType.video
                }
                else -> {
                    this.mimeType = MimeType.image
                }
            }
            this.thumbnailUrl = nonNull(albumEntity.thumbnailUrl)
            this.rawUrl = nonNull(albumEntity.rawUrl)
            this.originalFileName = nonNull(albumEntity.originalFileName)
            this.durationTime = nonNull(albumEntity.durationTime)
            this.createdDate = nonNull(albumEntity.createdDate).toLocalDateTime()
        }
    }

    val albums: ArrayList<AlbumModel>
    val cursorInfo: CursorInfoModel

    constructor(albumsResponseEntity: AlbumsResponseEntity?) {
        albums = ArrayList<AlbumModel>()
        albumsResponseEntity?.responseData?.let {
            it.forEach { albumEntity ->
                albums.add(AlbumModel(albumEntity))
            }
        }
        cursorInfo = CursorInfoModel(albumsResponseEntity?.cursorInfo)
    }
}
