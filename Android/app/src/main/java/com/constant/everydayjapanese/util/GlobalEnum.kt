package com.constant.everydayjapanese.util

import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.basic.EverydayJapaneseApplication
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

enum class IndexEnum(val id: Int, val title: String) {
    hiragana(0, EverydayJapaneseApplication.context.getString(R.string.hiragana)),
    katakana(1, EverydayJapaneseApplication.context.getString(R.string.katakana)),
    kanjiBookmark(2, EverydayJapaneseApplication.context.getString(R.string.favorites)),
    elementary1(3, EverydayJapaneseApplication.context.getString(R.string._1st_grade_elementary)),
    elementary2(4, EverydayJapaneseApplication.context.getString(R.string._2nd_grade_elementary)),
    elementary3(5, EverydayJapaneseApplication.context.getString(R.string._3rd_grade_elementary)),
    elementary4(6, EverydayJapaneseApplication.context.getString(R.string._4th_grade_elementary)),
    elementary5(7, EverydayJapaneseApplication.context.getString(R.string._5th_grade_elementary)),
    elementary6(8, EverydayJapaneseApplication.context.getString(R.string._6th_grade_elementary)),
    middle(9, EverydayJapaneseApplication.context.getString(R.string.middle_school)),
    vocabularyBookmark(10, EverydayJapaneseApplication.context.getString(R.string.favorites)),
    n5(11, EverydayJapaneseApplication.context.getString(R.string.n5)),
    n4(12, EverydayJapaneseApplication.context.getString(R.string.n4)),
    n3(13, EverydayJapaneseApplication.context.getString(R.string.n3)),
    n2(14, EverydayJapaneseApplication.context.getString(R.string.n2)),
    n1(15, EverydayJapaneseApplication.context.getString(R.string.n1)),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                IndexEnum.hiragana.id to IndexEnum.hiragana,
                IndexEnum.katakana.id to IndexEnum.katakana,
                IndexEnum.kanjiBookmark.id to IndexEnum.kanjiBookmark,
                IndexEnum.elementary1.id to IndexEnum.elementary1,
                IndexEnum.elementary2.id to IndexEnum.elementary2,
                IndexEnum.elementary3.id to IndexEnum.elementary3,
                IndexEnum.elementary4.id to IndexEnum.elementary4,
                IndexEnum.elementary5.id to IndexEnum.elementary5,
                IndexEnum.elementary6.id to IndexEnum.elementary6,
                IndexEnum.middle.id to IndexEnum.middle,
                IndexEnum.vocabularyBookmark.id to IndexEnum.vocabularyBookmark,
                IndexEnum.n5.id to IndexEnum.n5,
                IndexEnum.n4.id to IndexEnum.n4,
                IndexEnum.n3.id to IndexEnum.n3,
                IndexEnum.n2.id to IndexEnum.n2,
                IndexEnum.n1.id to IndexEnum.n1,
            )

        fun ofRaw(raw: Int): IndexEnum {
            return rawToEnum[raw] ?: IndexEnum.hiragana
        }
    }

    fun getFileName(): String {
        return when (this) {
            IndexEnum.hiragana, IndexEnum.katakana, IndexEnum.kanjiBookmark, IndexEnum.vocabularyBookmark -> ""
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

    fun getSection(): SectionEnum {
        when (this) {
            IndexEnum.hiragana, IndexEnum.katakana -> {
                return SectionEnum.hiraganakatagana
            }
            IndexEnum.kanjiBookmark,
            IndexEnum.elementary1,
            IndexEnum.elementary2,
            IndexEnum.elementary3,
            IndexEnum.elementary4,
            IndexEnum.elementary5,
            IndexEnum.elementary6,
            IndexEnum.middle,
            -> {
                return SectionEnum.kanji
            }
            IndexEnum.vocabularyBookmark,
            IndexEnum.n5,
            IndexEnum.n4,
            IndexEnum.n3,
            IndexEnum.n2,
            IndexEnum.n1,
            -> {
                return SectionEnum.vocabulary
            }
        }
    }

    fun getResourceId(): Int {
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

    fun getSettingTitle(): String {
        var retTitle = title
        if (this == IndexEnum.kanjiBookmark || this == IndexEnum.vocabularyBookmark) {
            retTitle = "${getSection().title} ${title}"
        }
        return retTitle
    }
}

enum class SectionEnum(val id: Int, val title: String) {
    hiraganakatagana(0, EverydayJapaneseApplication.context.getString(R.string.hiragana_katakana)),
    kanji(1, EverydayJapaneseApplication.context.getString(R.string.japanese_common_chinese)),
    vocabulary(2, EverydayJapaneseApplication.context.getString(R.string.jlpt_vocabulary)),
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

enum class FrequencyEnum(val id: Int, val title: String) {
    day(0, EverydayJapaneseApplication.context.getString(R.string.everyday)),
    hour(1, EverydayJapaneseApplication.context.getString(R.string.one_hour)),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                FrequencyEnum.day.id to FrequencyEnum.day,
                FrequencyEnum.hour.id to FrequencyEnum.hour,
            )

        fun ofRaw(raw: Int): FrequencyEnum {
            return rawToEnum[raw] ?: FrequencyEnum.day
        }
    }
}
