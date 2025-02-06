package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.LikeResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class LikeModel {
    val likeCount: Int
    val likeOn: Boolean

    constructor(likeResponseEntity: LikeResponseEntity) {
        this.likeCount = nonNull(likeResponseEntity.responseData?.likeCount)
        this.likeOn = nonNull(likeResponseEntity.responseData?.likeOn)
    }
}
