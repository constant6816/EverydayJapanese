package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.MemberAttendanceEntity
import com.constant.japaneseeveryday.util.AttendanceEnum
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

class MemberAttendanceModel {
    val member: MemberModel
    val startTime: LocalDateTime
    val endTime: LocalDateTime
    val attendance: AttendanceEnum

    constructor(memberAttendanceEntity: MemberAttendanceEntity) {
        this.member = MemberModel(memberAttendanceEntity.member)
        this.startTime = nonNull(memberAttendanceEntity.startTime).toLocalDateTime()
        this.endTime = nonNull(memberAttendanceEntity.endTime).toLocalDateTime()
        this.attendance = AttendanceEnum.ofRaw(nonNull(memberAttendanceEntity.attendance)) ?: AttendanceEnum.checkIn
    }
}
