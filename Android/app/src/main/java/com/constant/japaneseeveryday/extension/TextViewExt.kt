package com.constant.japaneseeveryday.extension

import android.content.res.ColorStateList
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.network.model.MemberModel
import com.constant.japaneseeveryday.util.PositionEnum
import com.constant.japaneseeveryday.util.RoleEnum

fun TextView.setTextOrHide(text: String?) {
    if (text.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        this.text = text
    }
}

fun TextView.setNickname(
    member: MemberModel?,
    defaultTextColor: Int,
    isShowAdmin: Boolean,
    isBig: Boolean,
) {
    member?.let { member ->
        when (member.status) {
            MemberModel.StatusEnum.valid -> {
                if (member.role == RoleEnum.admin && isShowAdmin) {
                    if (isBig) {
                        applyGUI(R.style.font_h4b, R.color.fg2)
                    } else {
                        applyGUI(R.style.font_p2b, R.color.fg2)
                    }
                    text = member.role.getName(context)
                    background = context.getDrawable(R.drawable.shape_rectangle_4dp)
                    clipToOutline = true
                    backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.fg_brand_xxlight))
                    setPadding(
                        context.resources.getDimension(R.dimen.space_x4s).toInt(),
                        context.resources.getDimension(R.dimen.space_x6s).toInt(),
                        context.resources.getDimension(R.dimen.space_x4s).toInt(),
                        context.resources.getDimension(R.dimen.space_x6s).toInt(),
                    )
                } else {
                    if (isBig) {
                        applyGUI(R.style.font_h4, defaultTextColor)
                    } else {
                        applyGUI(R.style.font_p2, defaultTextColor)
                    }
                    text = member.nickname
                    setBackgroundColor(context.getColor(R.color.transparent))
                    setPadding(0, 0, 0, 0)
                }
            }
            MemberModel.StatusEnum.withdrawn -> {
                applyGUI(R.style.font_p2, R.color.fg5)
                text = member.role.getName(context)
                setBackgroundColor(context.getColor(R.color.transparent))
                setPadding(0, 0, 0, 0)
            }
            MemberModel.StatusEnum.none -> {
                text = "none"
            }
            MemberModel.StatusEnum.deleted -> {
                text = "deleted"
            }
        }
    }
}

fun TextView.setPosition(member: MemberModel?) {
    when (member?.position) {
        PositionEnum.CEO -> {
            text = context.getString(R.string.ceo)
            backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.bg3))
            setTextColor(context.getColor(R.color.fg_brand))
        }
        PositionEnum.CoCeo -> {
            text = context.getString(R.string.coceo)
            backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.bg3))
            setTextColor(context.getColor(R.color.fg0))
        }
        PositionEnum.member -> {
            text = context.getString(R.string.member)
            backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.bg3))
            setTextColor(context.getColor(R.color.fg3))
        }
        PositionEnum.notMember -> {
            text = context.getString(R.string.not_member)
            backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.bg3))
            setTextColor(context.getColor(R.color.fg0))
        }
        else -> {
            text = "ERROR"
            backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.bg3))
            setTextColor(context.getColor(R.color.fg_black0))
        }
    }
}

fun TextView.asProperty(
    targetStrings: List<String>,
    font: Int,
    color: Int,
) {
    val fullText = this.text.toString()
    val spannableString = SpannableStringBuilder(fullText)

    for (targetString in targetStrings) {
        val startIndex = fullText.indexOf(targetString)
        if (startIndex != -1) {
            val endIndex = startIndex + targetString.length
            spannableString.setSpan(StyleSpan(font), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    this.text = spannableString
}
