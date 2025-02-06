package com.constant.japaneseeveryday.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.extension.getYoutubeId
import com.constant.japaneseeveryday.extension.getYoutubePreviewUrl
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class YoutubePreviewView : FrameLayout {
    // Public Inner Class, Struct, Enum, Interface
    interface OnClickCloseListener {
        fun onClickClose()
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private var onClickCloseListener: OnClickCloseListener? = null
    private lateinit var imageviewPreview: ImageView
    private lateinit var viewDark: View
    private lateinit var buttonClose: ImageButton
    private var youtubeLink: String? = null
    private var youtubeId: String? = null
    private var isTapGesture = false

    // Override Method or Basic Method
    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initializeViews()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initializeViews()
    }

    // Public Method
    fun enableRemove() {
        imageviewPreview.setOnClickListener {
            if (viewDark.visibility == View.VISIBLE) {
                viewDark.visibility = View.GONE
                buttonClose.visibility = View.GONE
            } else {
                viewDark.visibility = View.VISIBLE
                buttonClose.visibility = View.VISIBLE
            }
        }
    }

    fun setOnClickCloseListener(onClickCloseListener: OnClickCloseListener?) {
        this.onClickCloseListener = onClickCloseListener
    }

    fun setYoutubeLink(youtubeLink: String) {
        this.youtubeLink = youtubeLink
        youtubeId = youtubeLink.getYoutubeId()
        HHLog.d(TAG, "youtubeId = $youtubeId")
        Glide.with(context)
            .load(youtubeId?.getYoutubePreviewUrl())
            .placeholder(R.drawable.img_placeholder)
            .into(imageviewPreview)
    }

    fun getYoutubeId(): String? {
        return youtubeId
    }

    // Private Method
    private fun initializeViews() {
        HHLog.d(TAG, "initializeViews")
        if (context == null) {
            return
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_youtube_preview, this, true)
        imageviewPreview = findViewById(R.id.imageview_preview)
        viewDark = findViewById(R.id.view_dark)
        buttonClose = findViewById(R.id.button_close)
        buttonClose.setOnClickListener {
            onClickCloseListener?.onClickClose()
        }
        viewDark.visibility = View.GONE
        buttonClose.visibility = View.GONE
    }
}
