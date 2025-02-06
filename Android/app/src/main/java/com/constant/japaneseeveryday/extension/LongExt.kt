package com.constant.japaneseeveryday.extension

fun Long.formatDuration(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}
