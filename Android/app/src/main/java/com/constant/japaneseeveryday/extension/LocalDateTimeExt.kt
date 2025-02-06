package com.constant.japaneseeveryday.extension

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.util.Date

enum class FormatType(val value: String) {
    date1("yy.MM.dd"),
    date2("yyyy.MM.dd"),
    date3("M월 d일"),
    date4("yyyy-MM-dd"),
    date5("yyyy년MM월dd일"),
    date6("yyyy-MM-dd HH:mm:ss"),
    date7("yyyy.MM.dd HH:mm"),
    date8("a hh:mm"),
    date9("yyyy년 MM월 dd일"),

    date10("M월d일 E a hh:mm"),
    date11("yy년 M월 d일"),
    date12("M/d"),
    date13("M월"),
    date14("yyyy년 MM월"),
    date15("yyyy-MM-dd EEEE"),
    date16("MM-dd"),
    date17("yyyy-MM-dd HH:mm"),
    date18("MM.dd"),
    date19("yyyy.MM"),

    date100("a hh:mm"),
    date101("MM월dd일 a hh:mm"),
    date102("M월 d일(E)"),
    date110("yyMMdd_HHmmss"),
    date111("yyyy년 MM월 dd일 EEEE"),
    date112("a hh:mm"),
    date113("yyyy년 MM월dd일 a hh:mm:ss"),
    date114("yyyyMMddHHmmss"),
    dayOnly("dd"),
}

object LocalDateTimeExtensions {
    fun today(): LocalDateTime {
        return LocalDate.now().atStartOfDay()
    }

    fun date(
        year: Int,
        month: Int,
    ): LocalDateTime {
        return LocalDateTime.of(year, month, 1, 0, 0)
    }

    fun date(
        year: Int,
        month: Int,
        day: Int,
    ): LocalDateTime {
        return LocalDateTime.of(year, month, day, 0, 0)
    }
}

fun LocalDateTime.getDateString(formatType: FormatType): String {
    val simpleDateFormat = SimpleDateFormat(formatType.value)
    return simpleDateFormat.format(Date.from(this.atZone(ZoneId.systemDefault()).toInstant()))
}

fun LocalDateTime.beginningOfDay(): LocalDateTime {
    return this.toLocalDate().atStartOfDay()
}

fun LocalDateTime.beginningOfWeek(): LocalDateTime {
    return this.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
}

fun LocalDateTime.endOfWeek(): LocalDateTime {
    return this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
}

fun LocalDateTime.beginningOfMonth(): LocalDateTime {
    return this.withDayOfMonth(1)
}

fun LocalDateTime.endOfMonth(): LocalDateTime {
    val startOfMonth = this.beginningOfMonth()
    return startOfMonth.plusMonths(1).minusDays(1)
}

fun LocalDateTime.nextDay(): LocalDateTime {
    // 하루 뒤의 LocalDateTime 반환
    return this.plusDays(1)
}

fun LocalDateTime.nextDay(days: Int): LocalDateTime {
    // 주어진 일수 뒤의 LocalDateTime 반환
    return this.plusDays(days.toLong())
}

fun LocalDateTime.previousDay(): LocalDateTime {
    // 하루 전 LocalDateTime 반환
    return this.minusDays(1)
}

fun LocalDateTime.previousDay(days: Int): LocalDateTime {
    // 주어진 일수 전 LocalDateTime 반환
    return this.minusDays(days.toLong())
}

fun LocalDateTime.previousMonth(): LocalDateTime {
    // 자정으로 설정된 현재 날짜에서 이전 달의 첫 날을 반환
    return if (this.monthValue == 1) {
        // 1월일 경우, 전년 12월의 첫 번째 날로 설정
        this.withMonth(12).withYear(this.year - 1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
    } else {
        // 이전 달의 첫 번째 날로 설정
        this.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
    }
}

fun LocalDateTime.nextMonth(): LocalDateTime {
    return this.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
}

fun LocalDateTime.isSameYear(monthDate: LocalDateTime): Boolean {
    val year1Component = this.year
    val year2Component = monthDate.year

    return year1Component == year2Component
}

fun LocalDateTime.isSameMonth(monthDate: LocalDateTime): Boolean {
    val year1Component = this.year
    val month1Component = this.monthValue
    val year2Component = monthDate.year
    val month2Component = monthDate.monthValue

    return year1Component == year2Component && month1Component == month2Component
}

fun LocalDateTime.isSameDay(date: LocalDateTime): Boolean {
    val year1Component = this.year
    val month1Component = this.monthValue
    val day1Component = this.dayOfMonth
    val year2Component = date.year
    val month2Component = date.monthValue
    val day2Component = date.dayOfMonth

    return year1Component == year2Component && month1Component == month2Component && day1Component == day2Component
}

fun LocalDateTime.isToday(): Boolean {
    val today = LocalDate.now()
    return this.toLocalDate() == today
}

//
// fun LocalDateTime.monthOnly(): LocalDateTime {
//    return this.withDayOfMonth(1)
// }
fun LocalDateTime.getWeekday(): Int {
    return this.get(ChronoField.DAY_OF_WEEK)
}

fun LocalDateTime.toMilli(): Long {
    return this.toInstant(ZoneOffset.UTC).toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochMilli(this)
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun LocalDateTime.getHHBoardTime(): String {
    val timestampInMillis = Date.from(this.atZone(ZoneId.systemDefault()).toInstant()).time
    return DateUtils.getRelativeTimeSpanString(
        timestampInMillis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_SHOW_DATE,
    ).toString()
}

fun LocalDateTime.secondsBetween(endDate: LocalDateTime): Double {
    return Duration.between(this, endDate).seconds.toDouble()
}

fun LocalDateTime.minutesBetween(endDate: LocalDateTime): Double {
    return secondsBetween(endDate) / 60.0
}

fun LocalDateTime.hoursBetween(endDate: LocalDateTime): Double {
    return secondsBetween(endDate) / 3600.0
}

fun LocalDateTime.toSeconds(): Int {
    val startOfDay = this.toLocalDate().atStartOfDay()
    val seconds = Duration.between(startOfDay, this).seconds.toInt()
    return seconds
}
