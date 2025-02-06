package com.constant.japaneseeveryday.model

import android.graphics.Bitmap
import com.constant.japaneseeveryday.util.HHFileType
import com.constant.japaneseeveryday.util.MimeType

class DataModel {
    var filename: String
    var data: ByteArray
    var mimeType: MimeType

    constructor(filename: String, data: ByteArray, mimeType: MimeType) {
        this.filename = filename
        this.data = data
        this.mimeType = mimeType
    }
}

class HHFileModel {
    var fileType: HHFileType
    var image: Bitmap? = null
    var filename: String
    var data: ByteArray? = null // videoFromGallery일 경우 동영상 데이타
    var duration: Long? = null // videoFromGallery일 경우 시간

    // 편집일때 경우에만 존재
    var url: String? = null
    var fileId: Int? = null

    constructor(
        fileType: HHFileType,
        image: Bitmap?,
        filename: String,
        data: ByteArray?,
        duration: Long?,
        url: String?,
        fileId: Int?,
    ) {
        this.fileType = fileType
        this.image = image
        this.filename = filename
        this.data = data
        this.duration = duration
        this.url = url
        this.fileId = fileId
    }

    constructor() {
        fileType = HHFileType.imageFromCamera
        filename = ""
    }
}
