package com.constant.japaneseeveryday.extension

import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication

fun Int.toMinuteString(): String {
    // second --> 약XX분
    return String.format(JapaneseEverydayApplication.context.getString(R.string.about_before_minute), this / 60)
}

fun Int.toMinuteSecondString(): String {
    // second --> mm:ss
    return String.format("%02d:%02d", this / 60, this % 60)
}

fun Int.toHourMinuteEngString(): String {
    // second --> hh:mm
    return if (this >= 3600) {
        String.format("%dh %2dm", this / 3600, (this / 60) % 60)
    } else {
        String.format("%2dm", (this / 60) % 60)
    }
}

fun Int.toHourMinuteString(): String {
    // second --> hh:mm
    return if (this >= 3600) {
        String.format(JapaneseEverydayApplication.context.getString(R.string.hour_minute), this / 3600, (this / 60) % 60)
    } else {
        String.format(JapaneseEverydayApplication.context.getString(R.string.minute), (this / 60) % 60)
    }
}

fun Int.toHourMinuteEngStringFromMinute(): String {
    // minute --> hh:mm
    return if (this >= 60) {
        String.format("%dh %2dm", this / 60, this % 60)
    } else {
        String.format("%2dm", this % 60)
    }
}


fun Int.toHourMinuteStringFromMinute(): String {
    // minute --> hh:mm
    return if (this >= 60) {
        String.format(JapaneseEverydayApplication.context.getString(R.string.hour_minute), this / 60, this % 60)
    } else {
        String.format(JapaneseEverydayApplication.context.getString(R.string.minute), this % 60)
    }
}

fun Int.toHourMinuteSecondString(): String {
    // second --> hh:mm:ss
    val hour = this / 3600
    return if (hour > 0) {
        String.format("%02d:%02d:%02d", hour, (this / 60) % 60, this % 60)
    } else {
        String.format("%02d:%02d", (this / 60) % 60, this % 60)
    }
}

fun Int.toHourMinuteSecondString2(): String {
    // second --> hh:mm:ss
    val hour = this / 3600
    return if (hour > 0) {
        String.format("%02dh:%02dm:%02ds", hour, (this / 60) % 60, this % 60)
    } else {
        String.format("%02dm:%02ds", (this / 60) % 60, this % 60)
    }
}
