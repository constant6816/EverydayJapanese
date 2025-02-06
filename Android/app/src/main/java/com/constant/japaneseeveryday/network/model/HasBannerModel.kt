package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.HasBannerResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class HasBannerModel {
    var id: Int
    var imageUrl: String
    var linkUrl: String
    var isVisible: Boolean

    // MARK: - Override Method or Basic Method
    constructor(hasBannerResponseEntity: HasBannerResponseEntity) {
        this.id = nonNull(hasBannerResponseEntity.responseData?.id)
        this.imageUrl = nonNull(hasBannerResponseEntity.responseData?.imageUrl)
        this.linkUrl = nonNull(hasBannerResponseEntity.responseData?.linkUrl)
        this.isVisible = nonNull(hasBannerResponseEntity.responseData?.isVisible)
    }

    constructor() {
        id = 0
        imageUrl = ""
        linkUrl = ""
        isVisible = false
    }
}
