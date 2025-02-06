package com.constant.japaneseeveryday.scene.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.constant.japaneseeveryday.databinding.DialogHhdialogEditBinding
import com.constant.japaneseeveryday.extension.setTextOrHide

interface OnClickOkListener {
    fun onClickOk(
        dialog: DialogInterface?,
        id: Int,
        text: String,
    )
}

fun HHDialogEdit(
    context: Context?,
    titleText: String? = null,
    hintText: String? = null,
    okText: String? = null,
    okHandler: OnClickOkListener? = null,
    cancelText: String? = null,
    cancelHandler: DialogInterface.OnClickListener? = null,
) {
    val binding =
        DialogHhdialogEditBinding.inflate(LayoutInflater.from(context)).apply {
            textviewTitle.setTextOrHide(titleText)
            edittextEdit.hint = hintText
            buttonPositive.setTextOrHide(okText)
            buttonNegative.setTextOrHide(cancelText)
            constraintlayout.clipToOutline = true
        }
    val builder = AlertDialog.Builder(context).setView(binding.root)
    val dialog: AlertDialog = builder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)

    binding.buttonPositive.setOnClickListener {
        okHandler?.onClickOk(dialog, binding.buttonPositive.id, binding.edittextEdit.text.toString())
        dialog.dismiss()
    }
    binding.buttonNegative.setOnClickListener {
        cancelHandler?.onClick(dialog, binding.buttonPositive.id)
        dialog.dismiss()
    }
    dialog.show()
}
