package com.constant.japaneseeveryday.network.model

import android.os.Parcelable
import com.constant.japaneseeveryday.network.entity.FileEntity
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.nonNull
import kotlinx.parcelize.Parcelize

@Parcelize
class FileModel(
    var id: Int,
    var fileName: String,
    var mimeType: MimeType,
    var normalUrl: String,
    var rawUrl: String,
    var thumbnailUrl: String,
    var durationTime: Double,
) : Parcelable {
    constructor(fileEntity: FileEntity?) : this(
        nonNull(fileEntity?.id),
        nonNull(fileEntity?.fileName),
        MimeType.image,
        nonNull(fileEntity?.normalUrl),
        nonNull(fileEntity?.rawUrl),
        nonNull(fileEntity?.thumbnailUrl),
        nonNull(fileEntity?.durationTime),
    ) {
        when (fileEntity?.type) {
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
    }
}
