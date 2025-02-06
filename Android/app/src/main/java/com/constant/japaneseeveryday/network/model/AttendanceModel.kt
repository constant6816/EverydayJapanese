package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.AttendanceEntity
import com.constant.japaneseeveryday.util.AttendanceEnum
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class AttendanceModel {
    val startTime: LocalDateTime
    val endTime: LocalDateTime
    val attendance: AttendanceEnum

    constructor(attendanceEntity: AttendanceEntity) {
        this.startTime = nonNull(attendanceEntity.startTime).toLocalDateTime()
        this.endTime = nonNull(attendanceEntity.endTime).toLocalDateTime()
        this.attendance = AttendanceEnum.ofRaw(nonNull(attendanceEntity.attendance)) ?: AttendanceEnum.checkIn
    }
}
