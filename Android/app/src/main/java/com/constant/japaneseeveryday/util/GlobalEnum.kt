package com.constant.japaneseeveryday.util

import android.content.Context
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.extension.LATER
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

enum class ServerEnum(val shortName: String) {
    production("PR"),
    stage("ST"),
    develop("DEV"),
    home("HOME"),
    office("OFFICE"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                ServerEnum.production.shortName to ServerEnum.production,
                ServerEnum.stage.shortName to ServerEnum.stage,
                ServerEnum.develop.shortName to ServerEnum.develop,
                ServerEnum.home.shortName to ServerEnum.home,
                ServerEnum.office.shortName to ServerEnum.office,
            )

        fun ofRaw(raw: String): ServerEnum {
            return rawToEnum[raw] ?: ServerEnum.production
        }
    }
}

enum class WithdrawalReason(val value: Int) {
    notUseFrequent(1),
    deletePersonalInfo(2),
    inconvenience(3),
    etc(4),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                WithdrawalReason.notUseFrequent.value to WithdrawalReason.notUseFrequent,
                WithdrawalReason.deletePersonalInfo.value to WithdrawalReason.deletePersonalInfo,
                WithdrawalReason.inconvenience.value to WithdrawalReason.inconvenience,
                WithdrawalReason.etc.value to WithdrawalReason.etc,
            )

        fun ofRaw(raw: Int): WithdrawalReason {
            return rawToEnum[raw] ?: WithdrawalReason.notUseFrequent
        }
    }
}

enum class MimeType(val value: String) {
    image("image/jpeg"),
    video("video/mp4"),
    ;

    fun getShortString(): String {
        return when (this) {
            image -> "IMAGE"
            video -> "VIDEO"
        }
    }

    fun toMediaType(): MediaType? {
        return when (this) {
            image -> "image/*".toMediaTypeOrNull()
            video -> "video/*".toMediaTypeOrNull()
        }
    }

    companion object {
        val rawToEnum =
            mapOf(
                image.value to MimeType.image,
                video.value to MimeType.video,
            )

        fun ofRaw(raw: String): MimeType {
            return rawToEnum[raw] ?: MimeType.image
        }
    }
}

enum class ClubType(val value: String) {
    board("BOARD"),
    notice("NOTICE"),
    qna("QNA"),
    reserved("RESERVED"),
    club("CLUB"),

    ;

    companion object {
        val rawToEnum =
            mapOf(
                ClubType.board.value to ClubType.board,
                ClubType.notice.value to ClubType.notice,
                ClubType.qna.value to ClubType.qna,
            )

        fun ofRaw(raw: String): ClubType {
            return rawToEnum[raw] ?: ClubType.board
        }
    }

    fun getId(): Int {
        return when (this) {
            ClubType.board -> 1
            ClubType.notice -> 2
            ClubType.qna -> 3
            else -> 11
        }
    }
}

enum class HHFileType {
    imageFromCamera,
    imageFromGallery,
    videoFromGallery,

    // 편집일때
    imageFromUrl,
    videoFromUrl,
    ;

    fun getMimeType(): MimeType {
        return when (this) {
            imageFromCamera, imageFromGallery, imageFromUrl -> MimeType.image
            videoFromGallery, videoFromUrl -> return MimeType.image
        }
    }
}

enum class RoleEnum(val value: String) {
    admin("ADMIN"),
    manager("MANAGER"),
    user("USER"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                RoleEnum.admin.value to RoleEnum.admin,
                RoleEnum.manager.value to RoleEnum.manager,
                RoleEnum.user.value to RoleEnum.user,
            )

        fun ofRaw(raw: String): RoleEnum {
            return rawToEnum[raw] ?: RoleEnum.user
        }
    }

    fun getName(context: Context): String {
        return when (this) {
            admin -> context.getString(R.string.operators)
            manager -> context.getString(R.string.manager)
            user -> context.getString(R.string.member)
        }
    }
}

enum class AccessLevelEnum(val value: String) {
    `private`("PRIVATE"),
    `public`("PUBLIC"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                AccessLevelEnum.private.value to AccessLevelEnum.private,
                AccessLevelEnum.public.value to AccessLevelEnum.public,
            )

        fun ofRaw(raw: String): AccessLevelEnum {
            return rawToEnum[raw] ?: AccessLevelEnum.private
        }
    }
}

enum class PositionEnum(val value: String) {
    CEO("CEO"),
    CoCeo("COCEO"),
    member("MEMBER"),
    notMember("NOT_MEMBER"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                PositionEnum.CEO.value to PositionEnum.CEO,
                PositionEnum.CoCeo.value to PositionEnum.CoCeo,
                PositionEnum.member.value to PositionEnum.member,
                PositionEnum.notMember.value to PositionEnum.notMember,
            )

        fun ofRaw(raw: String): PositionEnum {
            return rawToEnum[raw] ?: PositionEnum.notMember
        }
    }

    fun getName(context: Context): String {
        return when (this) {
            CEO -> context.getString(R.string.ceo)
            CoCeo -> context.getString(R.string.coceo)
            member -> context.getString(R.string.member)
            notMember -> context.getString(R.string.not_member)
        }
    }

    fun getColor(context: Context): Int {
        return when (this) {
            CEO -> context.getColor(R.color.fg_brand)
            CoCeo -> context.getColor(R.color.fg_white0)
            member -> context.getColor(R.color.fg_white4)
            notMember -> context.getColor(R.color.fg_black0)
        }
    }
}

enum class AttendanceEnum(val value: String) {
    checkIn("CHECK_IN"),
    checkOut("CHECK_OUT"),
    cancel("CANCEL"),
    manual("MANUAL"),
    requesting("REQUESTING"),
    reject("REJECT"),
    ;

    fun getStatus(): Pair<String, Int> {
        when (this) {
            AttendanceEnum.checkIn -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.checking_in),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_orange),
                )
            }
            AttendanceEnum.checkOut -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.attendance_completed),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_green),
                )
            }
            AttendanceEnum.cancel -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.attendance_cancellation),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_red),
                )
            }
            AttendanceEnum.manual -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.manual_input),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_blue),
                )
            }
            AttendanceEnum.requesting -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.waiting_approval),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_orange),
                )
            }
            AttendanceEnum.reject -> {
                return Pair<String, Int>(
                    JapaneseEverydayApplication.context.getString(R.string.approval_rejection),
                    JapaneseEverydayApplication.context.getColor(R.color.fg_red),
                )
            }
        }
    }

    companion object {
        val rawToEnum =
            mapOf(
                AttendanceEnum.checkIn.value to AttendanceEnum.checkIn,
                AttendanceEnum.checkOut.value to AttendanceEnum.checkOut,
                AttendanceEnum.cancel.value to AttendanceEnum.cancel,
                AttendanceEnum.manual.value to AttendanceEnum.manual,
                AttendanceEnum.requesting.value to AttendanceEnum.requesting,
                AttendanceEnum.reject.value to AttendanceEnum.reject,
            )

        fun ofRaw(raw: String): AttendanceEnum? {
            return rawToEnum[raw]
        }
    }
}

enum class ApprovalType(val value: String) {
    attendance("ATTENDANCE"),
    member("MEMBER"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                ApprovalType.attendance.value to ApprovalType.attendance,
                ApprovalType.member.value to ApprovalType.member,
            )

        fun ofRaw(raw: String): ApprovalType {
            return rawToEnum[raw] ?: ApprovalType.attendance
        }
    }
}

enum class StatusEnum(val value: String) {
    valid("VALID"),
    requesting("REQUESTING"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                StatusEnum.valid.value to StatusEnum.valid,
                StatusEnum.requesting.value to StatusEnum.requesting,
            )

        fun ofRaw(raw: String): StatusEnum {
            return rawToEnum[raw] ?: StatusEnum.valid
        }
    }
}
enum class IndexEnum(val id:Int, val title: String) {
    bookmark(0, "즐겨찾기".LATER()),
    hiragana(1, "히라가나".LATER()),
    katakana(2, "가타카나".LATER()),
    elementary1(3, "소학교 1학년".LATER()),
    elementary2(4, "소학교 2학년".LATER()),
    elementary3(5, "소학교 3학년".LATER()),
    elementary4(6, "소학교 4학년".LATER()),
    elementary5(7, "소학교 5학년".LATER()),
    elementary6(8, "소학교 6학년".LATER()),
    middle(9, "중학교".LATER()),
    n5(10, "N5".LATER()),
    n4(11, "N4".LATER()),
    n3(12, "N3".LATER()),
    n2(13, "N2".LATER()),
    n1(14, "N1".LATER()),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                IndexEnum.bookmark.id to IndexEnum.bookmark,
                IndexEnum.hiragana.id to IndexEnum.hiragana,
                IndexEnum.katakana.id to IndexEnum.katakana,
                IndexEnum.elementary1.id to IndexEnum.elementary1,
                IndexEnum.elementary2.id to IndexEnum.elementary2,
                IndexEnum.elementary3.id to IndexEnum.elementary3,
                IndexEnum.elementary4.id to IndexEnum.elementary4,
                IndexEnum.elementary5.id to IndexEnum.elementary5,
                IndexEnum.elementary6.id to IndexEnum.elementary6,
                IndexEnum.middle.id to IndexEnum.middle,
                IndexEnum.n5.id to IndexEnum.n5,
                IndexEnum.n4.id to IndexEnum.n4,
                IndexEnum.n3.id to IndexEnum.n3,
                IndexEnum.n2.id to IndexEnum.n2,
                IndexEnum.n1.id to IndexEnum.n1,
            )

        fun ofRaw(raw: Int): IndexEnum {
            return rawToEnum[raw] ?: IndexEnum.bookmark
        }
    }

    fun getFileName(): String {
        return when (this) {
            IndexEnum.bookmark, IndexEnum.hiragana, IndexEnum.katakana -> ""
            IndexEnum.elementary1 -> "kanji1.json"
            IndexEnum.elementary2 -> "kanji2.json"
            IndexEnum.elementary3 -> "kanji3.json"
            IndexEnum.elementary4 -> "kanji4.json"
            IndexEnum.elementary5 -> "kanji5.json"
            IndexEnum.elementary6 -> "kanji6.json"
            IndexEnum.middle -> "kanji7.json"
            IndexEnum.n5 -> "n5.json"
            IndexEnum.n4 -> "n4.json"
            IndexEnum.n3 -> "n3.json"
            IndexEnum.n2 -> "n2.json"
            IndexEnum.n1 -> "n1.json"
        }
    }

    fun getSection(): SectionEnum? {
        when(this) {
            IndexEnum.bookmark -> {
                return null
            }
            IndexEnum.hiragana, IndexEnum.katakana -> {
                return SectionEnum.hiraganakatagana
            }
            IndexEnum.elementary1,
            IndexEnum.elementary2,
            IndexEnum.elementary3,
            IndexEnum.elementary4,
            IndexEnum.elementary5,
            IndexEnum.elementary6,
            IndexEnum.middle -> {
                return SectionEnum.kanji
            }
            IndexEnum.n5,
            IndexEnum.n4,
            IndexEnum.n3,
            IndexEnum.n2,
            IndexEnum.n1 -> {
                return SectionEnum.vocabulary
            }
        }
    }

    fun getResourceId() : Int {
        val section = getSection()
        when (section) {
            SectionEnum.hiraganakatagana -> {
                return R.drawable.hiraganakatakana
            }
            SectionEnum.kanji -> {
                return R.drawable.kanji
            }
            SectionEnum.vocabulary -> {
                return R.drawable.jlpt
            }
            else -> {
                return 0
            }
        }
    }

}
enum class SectionEnum(val id:Int, val title: String) {
    hiraganakatagana(0, "히라가나 가타카나 표".LATER()),
    kanji(1, "일본 상용한자".LATER()),
    vocabulary(2, "JLPT 단어장".LATER()),
    ;


    companion object {
        val rawToEnum =
            mapOf(
                SectionEnum.hiraganakatagana.id to SectionEnum.hiraganakatagana,
                SectionEnum.kanji.id to SectionEnum.kanji,
                SectionEnum.vocabulary.id to SectionEnum.vocabulary,

            )

        fun ofRaw(raw: Int): SectionEnum {
            return rawToEnum[raw] ?: SectionEnum.hiraganakatagana
        }
    }
}