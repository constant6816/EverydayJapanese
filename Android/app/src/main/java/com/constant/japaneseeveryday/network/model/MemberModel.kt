package com.constant.japaneseeveryday.network.model

import android.content.Context
import android.os.Parcelable
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.network.entity.MemberEntity
import com.constant.japaneseeveryday.util.PositionEnum
import com.constant.japaneseeveryday.util.RoleEnum
import com.constant.japaneseeveryday.util.nonNull
import kotlinx.parcelize.Parcelize

@Parcelize
class MemberModel(
    var id: Int,
    var nickname: String,
    var introduction: String,
    var status: StatusEnum,
    var email: String,
    var profileUrl: String?,
    var role: RoleEnum,
    var isEditable: Boolean?,
    var isMe: Boolean?,
    var position: PositionEnum?,
) : Parcelable {
    enum class StatusEnum(val value: String) {
        none("none"),
        valid("VALID"),
        withdrawn("WITHDRAWN"),
        deleted("DELETED"),
        ;

        companion object {
            val rawToEnum =
                mapOf(
                    none.value to StatusEnum.none,
                    valid.value to StatusEnum.valid,
                    withdrawn.value to StatusEnum.withdrawn,
                    deleted.value to StatusEnum.deleted,
                )

            fun ofRaw(raw: String): StatusEnum {
                return rawToEnum[raw] ?: StatusEnum.none
            }
        }
    }

    constructor() : this(
        0,
        "",
        "",
        StatusEnum.none,
        "",
        null,
        RoleEnum.user,
        null,
        null,
        null,
    ) {
    }

    constructor(memberEntity: MemberEntity?) : this(
        nonNull(memberEntity?.id),
        nonNull(memberEntity?.nickname),
        nonNull(memberEntity?.introduction),
        StatusEnum.ofRaw(nonNull(memberEntity?.status)),
        nonNull(memberEntity?.email),
        memberEntity?.profileUrl,
        RoleEnum.ofRaw(nonNull(memberEntity?.role)),
        memberEntity?.isEditable,
        memberEntity?.isMe,
        PositionEnum.ofRaw(nonNull(memberEntity?.position)),
    ) {
    }

    fun getDisplayName(context: Context): String {
        return when (status) {
            StatusEnum.valid -> return if (role == RoleEnum.admin) {
                role.getName(context)
            } else {
                nickname
            }
            StatusEnum.withdrawn -> context.getString(R.string.board_unknown_user)
            StatusEnum.none -> "none"
            StatusEnum.deleted -> "deleted"
        }
    }

    fun getNameColor(context: Context): Int {
        return when (status) {
            StatusEnum.valid -> context.getColor(R.color.fg2)
            StatusEnum.withdrawn -> context.getColor(R.color.fg5)
            StatusEnum.none -> context.getColor(R.color.fg5)
            StatusEnum.deleted -> context.getColor(R.color.fg5)
        }
    }

    override fun toString(): String {
        return "MemberModel(id=$id, nickname='$nickname', status='$status', email='$email', profileUrl=$profileUrl, isEditable=$isEditable)"
    }
}
