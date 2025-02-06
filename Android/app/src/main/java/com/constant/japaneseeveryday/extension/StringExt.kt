package com.constant.japaneseeveryday.extension

import com.constant.japaneseeveryday.util.GlobalConst
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object StringExtensions {
    fun composeAddress(
        province: String,
        city: String,
        area: String,
    ): String {
        if (province == city) {
            return String.format("%s %s", city, area)
        } else {
            return String.format("%s %s %s", province, city, area)
        }
    }
}

fun String.getYoutubeId(): String? {
    val typePattern = "(?:(?:\\.be\\/|embed\\/|v\\/|\\?v=|\\&v=|\\/videos\\/)|(?:[\\w+]+#\\w/\\w(?:/[\\w]+)?/\\w/))([\\w-_]+)"
    val regex = Regex(typePattern, RegexOption.IGNORE_CASE)
    return regex.find(this)?.groups?.get(1)?.value
}

fun String.getYoutubePreviewUrl(): String? {
    return "https://img.youtube.com/vi/$this/mqdefault.jpg"
}

fun String.toOptionalLocalDateTime(): LocalDateTime? {
    try {
        val formatter = DateTimeFormatter.ofPattern(GlobalConst.DATE_FORMAT_SERVER1)
        return LocalDateTime.parse(this, formatter)
    } catch (e: Exception) {
        try {
            // 간혹 00초가 안 넘어오는 case가 있음
            val formatter = DateTimeFormatter.ofPattern(GlobalConst.DATE_FORMAT_SERVER2)
            return LocalDateTime.parse(this, formatter)
        } catch (e: Exception) {
            try {
                val formatter = DateTimeFormatter.ofPattern(GlobalConst.DATE_FORMAT_SERVER3)
                return LocalDate.parse(this, formatter).atStartOfDay()
            } catch (e: Exception) {
                return null
            }
        }
    }
}

fun String.toLocalDateTime(): LocalDateTime {
    return toOptionalLocalDateTime() ?: LocalDateTime.now()
}

fun String.LATER(): String {
    return this
}

fun String.extractCharacters(letterCount: Int): String {
    if (this.count() <= letterCount) {
        return this
    }
    return this.take(letterCount)
}
