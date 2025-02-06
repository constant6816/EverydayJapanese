package com.constant.japaneseeveryday.util

import com.constant.japaneseeveryday.network.entity.AddressResponseEntity
import com.constant.japaneseeveryday.network.entity.AnnouncesResponseEntity
import com.constant.japaneseeveryday.network.entity.ApprovalsResponseEntity
import com.constant.japaneseeveryday.network.entity.ChatMessageEntity
import com.constant.japaneseeveryday.network.entity.ChatRoomsResponseEntity
import com.constant.japaneseeveryday.network.entity.CommentsResponseEntity
import com.constant.japaneseeveryday.network.entity.DailyDurationEntity
import com.constant.japaneseeveryday.network.entity.FileEntity
import com.constant.japaneseeveryday.network.entity.MemberAttendanceEntity
import com.constant.japaneseeveryday.network.entity.MemberDailyDurationsResponseEntity
import com.constant.japaneseeveryday.network.entity.MemberDurationsResponseEntity
import com.constant.japaneseeveryday.network.entity.MemberEntity
import com.constant.japaneseeveryday.network.entity.RepliesResponseEntity
import com.constant.japaneseeveryday.network.model.CommentModel
import com.constant.japaneseeveryday.network.model.FileModel

// ----------------------------------------------------
// Public Method
fun nonNull(text: String?): String {
    return text ?: ""
}

fun nonNull(obj: Object?): Object {
    return obj ?: Object()
}

fun nonNull(i: Int?): Int {
    return i ?: 0
}

fun nonNull(i: Long?): Long {
    return i ?: 0
}

fun nonNull(b: Boolean?): Boolean {
    return b ?: false
}

fun nonNull(d: Double?): Double {
    return d ?: 0.0
}

fun nonNull(a: ByteArray?): ByteArray {
    return a ?: ByteArray(0)
}

fun nonNull(a: Array<Any>?): Array<Any> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<FileEntity>?): Array<FileEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<ChatMessageEntity>?): Array<ChatMessageEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<MemberEntity>?): Array<MemberEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<CommentsResponseEntity.CommentEntity>?): Array<CommentsResponseEntity.CommentEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<RepliesResponseEntity.ReplyEntity>?): Array<RepliesResponseEntity.ReplyEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<ChatRoomsResponseEntity.ChatRoomEntity>?): Array<ChatRoomsResponseEntity.ChatRoomEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<AddressResponseEntity.AddressEntity>?): Array<AddressResponseEntity.AddressEntity> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<String>?): Array<String> {
    return a ?: arrayOf()
}

fun nonNull(a: Array<AnnouncesResponseEntity.AnnounceEntity>?): Array<AnnouncesResponseEntity.AnnounceEntity> {
    return a ?: arrayOf()
}

@JvmName("callFromDailyDurationEntity")
fun nonNull(a: List<DailyDurationEntity>?): List<DailyDurationEntity> {
    return a ?: ArrayList<DailyDurationEntity>()
}

@JvmName("callFromMemberDurationEntity")
fun nonNull(a: List<MemberDurationsResponseEntity.MemberDurationEntity>?): List<MemberDurationsResponseEntity.MemberDurationEntity> {
    return a ?: ArrayList<MemberDurationsResponseEntity.MemberDurationEntity>()
}

@JvmName("callFromMemberDailyDurationEntity")
fun nonNull(
    a: List<MemberDailyDurationsResponseEntity.MemberDailyDurationEntity>?,
): List<MemberDailyDurationsResponseEntity.MemberDailyDurationEntity> {
    return a ?: ArrayList<MemberDailyDurationsResponseEntity.MemberDailyDurationEntity>()
}

@JvmName("callFromMemberAttendanceEntity")
fun nonNull(a: List<MemberAttendanceEntity>?): List<MemberAttendanceEntity> {
    return a ?: ArrayList<MemberAttendanceEntity>()
}

@JvmName("callFromApprovalEntity")
fun nonNull(a: List<ApprovalsResponseEntity.ApprovalEntity>?): List<ApprovalsResponseEntity.ApprovalEntity> {
    return a ?: ArrayList<ApprovalsResponseEntity.ApprovalEntity>()
}

@JvmName("callFromString")
fun nonNull(a: ArrayList<String>?): ArrayList<String> {
    return a ?: ArrayList<String>()
}

@JvmName("callFromCommentModel")
fun nonNull(a: ArrayList<CommentModel>?): ArrayList<CommentModel> {
    return a ?: ArrayList<CommentModel>()
}

@JvmName("callFromFileModel")
fun nonNull(a: ArrayList<FileModel>?): ArrayList<FileModel> {
    return a ?: ArrayList<FileModel>()
}
