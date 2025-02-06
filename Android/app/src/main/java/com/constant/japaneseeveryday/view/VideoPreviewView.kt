package com.constant.japaneseeveryday.view

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.nonNull
import java.util.concurrent.TimeUnit

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class VideoPreviewView : FrameLayout {
    // Public Inner Class, Struct, Enum, Interface
    inner class VideoInfoTask(
        private val imageviewPreview: ImageView,
        private val textviewTime: TextView,
    ) : AsyncTask<String, Void, Pair<Bitmap?, Long?>>() {
        override fun doInBackground(vararg params: String?): Pair<Bitmap?, Long?>? {
            val videoUrl = params[0]
            return try {
                getVideoInfo(videoUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Pair<Bitmap?, Long?>?) {
            super.onPostExecute(result)
            if (result != null) {
                result.first?.let { bitmap ->
                    // 사용자가 빠르게 왔다 갔다 할 경우 Activity가 사라져서 context 가 사라지는 버그가 있어,
                    // Application Context 를 사용한다.
                    Glide.with(JapaneseEverydayApplication.context)
                        .load(bitmap)
                        .transform(CenterCrop(), RoundedCorners(resources.getDimension(R.dimen.corner_radius_m).toInt()))
                        .placeholder(R.drawable.img_placeholder)
                        .into(imageviewPreview)
                }
                result.second?.let {
                    val duration = formatDurationMicroSec(it)
                    textviewTime.text = duration.toString()
                    textviewTime.visibility = View.VISIBLE
                }
            } else {
                HHLog.e(TAG, "ERROR to get image & play time")
            }
        }

        private fun getVideoInfo(videoUrl: String?): Pair<Bitmap?, Long?>? {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoUrl, HashMap<String, String>())
            var bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            // 강제로 사이즈 줄여도 속도가 나아지지 않네.
//            bitmap = bitmap?.let {
//                Bitmap.createScaledBitmap(it, 30, 30, false)
//            }
            val durationMicroSec =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()

            HHLog.d(TAG, "durationMicroSec = $durationMicroSec")
            retriever.release()
            return Pair(bitmap, durationMicroSec)
        }
    }

    // companion object
    companion object {
        fun formatDurationMicroSec(durationMicroSec: Long): String {
            val durationSeconds = TimeUnit.MICROSECONDS.toMillis(durationMicroSec)
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

        fun formatDurationSec(durationTime: Double): String {
            val minutes = durationTime.toLong() / 60
            val seconds = durationTime.toLong() % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var imageviewPreview: ImageView
    private lateinit var imageviewPlay: ImageView
    private lateinit var textviewTime: TextView

    private lateinit var videoUrl: String
    private lateinit var thumbnailUrl: String
    private var durationTime: Double = 0.0

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

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int,
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Update imageviewPlay size based on the new size
        val playButtonSize = (w * 0.3).toInt() // Example: 10% of the width of VideoPreviewView
        imageviewPlay.layoutParams.width = playButtonSize
        imageviewPlay.layoutParams.height = playButtonSize

        HHLog.d(TAG, "VideoPreviewView - onSizeChanged() w = $w")

        if (w < 400) {
            val verticalPadding = 0
            val horizontalPadding = 30
            textviewTime.setTextAppearance(R.style.font_p4)
            textviewTime.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        } else if (w < 800) {
            val verticalPadding = 0
            val horizontalPadding = 40
            textviewTime.setTextAppearance(R.style.font_p3)
            textviewTime.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        } else if (w < 1200) {
            val verticalPadding = 0
            val horizontalPadding = 50
            textviewTime.setTextAppearance(R.style.font_p2)
            textviewTime.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        } else {
            val verticalPadding = 0
            val horizontalPadding = 60
            textviewTime.setTextAppearance(R.style.font_p1)
            textviewTime.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        }
        // textviewTime.requestLayout();
        // textviewTime.invalidate();
        // 강제로 레이아웃을 재측정해서 재배치한다.
        textviewTime.measure(
            View.MeasureSpec.makeMeasureSpec(textviewTime.getWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(textviewTime.getHeight(), View.MeasureSpec.EXACTLY),
        )
        textviewTime.layout(textviewTime.getLeft(), textviewTime.getTop(), textviewTime.getRight(), textviewTime.getBottom())
    }
    // Public Method
//    fun setVideoUrl(videoUrl: String) {
//        this.videoUrl = videoUrl
//        // 비동기적으로 비디오 프레임 캡처 작업 수행
//        VideoInfoTask(imageviewPreview, textviewTime).execute(videoUrl)
//    }

    fun setThumbnailUrl(
        thumbnailUrl: String,
        isRoundedCorners: Boolean,
    ) {
        this.thumbnailUrl = thumbnailUrl
        Glide.with(context).load(thumbnailUrl).apply {
            if (isRoundedCorners) { // Replace shouldApplyRoundedCorners with your actual condition
                transform(CenterCrop(), RoundedCorners(resources.getDimension(R.dimen.corner_radius_m).toInt()))
            } else {
                transform(CenterCrop())
            }
            placeholder(R.drawable.img_placeholder)
        }.into(imageviewPreview)
    }

    fun setDurationTime(durationTime: Double) {
        this.durationTime = durationTime
        textviewTime.visibility = View.VISIBLE
        textviewTime.text = formatDurationSec(durationTime)
    }

    fun setVisible(
        mimeType: MimeType,
        isPlayBtn: Boolean = true,
    ) {
        when (mimeType) {
            MimeType.image -> {
                textviewTime.visibility = View.GONE
                imageviewPlay.visibility = View.GONE
            }
            MimeType.video -> {
                textviewTime.visibility = View.VISIBLE
                if (isPlayBtn) {
                    imageviewPlay.visibility = View.VISIBLE
                } else {
                    imageviewPlay.visibility = View.GONE
                }
            }
        }
    }

    // Private Method
    private fun initializeViews() {
        HHLog.d(TAG, "initializeViews")
        if (context == null) {
            return
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_video_preview, this, true)
        imageviewPreview = findViewById(R.id.imageview_preview)
        imageviewPlay = findViewById(R.id.imageview_play)
        textviewTime = findViewById(R.id.textview_time)
        textviewTime.visibility = View.GONE
    }
}
