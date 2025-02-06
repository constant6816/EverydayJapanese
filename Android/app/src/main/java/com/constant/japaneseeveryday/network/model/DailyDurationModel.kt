package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.DailyDurationEntity
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class DailyDurationModel {
    val date: LocalDateTime
    val seconds: Int

    constructor(dailyDurationEntity: DailyDurationEntity) {
        this.date = nonNull(dailyDurationEntity.date).toLocalDateTime()
        this.seconds = nonNull(dailyDurationEntity.seconds)
    }
}
