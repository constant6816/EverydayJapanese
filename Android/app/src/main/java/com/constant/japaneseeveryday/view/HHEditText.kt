package com.constant.japaneseeveryday.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.constant.japaneseeveryday.R

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class HHEditText : AppCompatEditText, TextWatcher, OnTouchListener, OnFocusChangeListener {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    private lateinit var clearDrawable: Drawable
    private var onTouchListener: OnTouchListener? = null
    private var focusChangeListener: OnFocusChangeListener? = null

    // Override Method or Basic Method
    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeViews()
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        this.focusChangeListener = onFocusChangeListener
    }

    override fun onTouch(
        view: View?,
        event: MotionEvent?,
    ): Boolean {
        var x = event?.x

        if (x != null) {
            if (clearDrawable.isVisible && x > width - paddingRight - clearDrawable.intrinsicWidth) {
                if (event?.action == MotionEvent.ACTION_UP) {
                    error = null
                    text = null
                }
                return true
            }
        }

        return onTouchListener?.onTouch(view, event) ?: false
    }

    override fun onFocusChange(
        view: View?,
        hasFocus: Boolean,
    ) {
        if (hasFocus) {
            setClearIconVisible(text?.isNotEmpty() ?: false)
        } else {
            setClearIconVisible(false)
        }

        if (focusChangeListener != null) {
            focusChangeListener?.onFocusChange(view, hasFocus)
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int,
    ) {
        if (isFocused) {
            setClearIconVisible(text?.isNotEmpty() ?: false)
        }
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    // Public Method
    // Private Method

    private fun initializeViews() {
        isSingleLine = true // 한 줄로 설정
        var drawable = ContextCompat.getDrawable(context, R.drawable.btn_close_circle)
        drawable?.run {
            clearDrawable = DrawableCompat.wrap(this)
//            DrawableCompat.setTintList(clearDrawable, hintTextColors)
            clearDrawable.setBounds(0, 0, clearDrawable.intrinsicWidth, clearDrawable.intrinsicHeight)

            setClearIconVisible(false)

            super.setOnTouchListener(this@HHEditText)
            super.setOnFocusChangeListener(this@HHEditText)
            addTextChangedListener(this@HHEditText)
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        clearDrawable.setVisible(visible, false)
        setCompoundDrawables(null, null, getRightDrawable(visible), null)
    }

    private fun getRightDrawable(visible: Boolean): Drawable? = if (visible) clearDrawable else null
}
