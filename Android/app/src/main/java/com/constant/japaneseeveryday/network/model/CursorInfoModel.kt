package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.CursorInfoEntity
import com.constant.japaneseeveryday.util.nonNull

class CursorInfoModel {
    var cursor: Int?
    var hasNext: Boolean

    constructor(cursorInfoEntity: CursorInfoEntity?) {
        this.cursor = cursorInfoEntity?.cursor
        this.hasNext = nonNull(cursorInfoEntity?.hasNext)
    }

    constructor(cursor: Int?, hasNext: Boolean) {
        this.cursor = cursor
        this.hasNext = hasNext
    }
}
