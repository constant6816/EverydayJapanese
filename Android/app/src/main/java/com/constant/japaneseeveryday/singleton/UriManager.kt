package com.constant.japaneseeveryday.singleton

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.constant.japaneseeveryday.model.HHFileModel
import com.constant.japaneseeveryday.util.GlobalConst
import com.constant.japaneseeveryday.util.HHFileType
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.nonNull
import io.reactivex.rxjava3.core.Observable
import java.io.ByteArrayOutputStream
import java.io.InputStream

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class UriManager {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: UriManager

        fun getInstance(): UriManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = UriManager()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun decodeImages(
        context: Context,
        uris: List<Uri>,
    ): Observable<ArrayList<HHFileModel>> {
        return Observable.create<ArrayList<HHFileModel>> { emitter ->
            val files: ArrayList<HHFileModel> = ArrayList<HHFileModel>()
            if (uris.isEmpty()) {
                emitter.onNext(files)
                emitter.onComplete()
                return@create
            }

            HHLog.d(TAG, "result $uris")
            uris.forEachIndexed { index, uri ->
                val filename =
                    getFileName(context, uri) ?: "xxx$index.${GlobalConst.extJPG}"
                val image = getImageThumbnail(context, uri)
                val rawData = getByteArray(context, uri)
                HHLog.d(TAG, "filename = $filename")
                files.add(
                    HHFileModel(
                        HHFileType.imageFromGallery,
                        image,
                        filename,
                        rawData,
                        null,
                        null,
                        null,
                    ),
                )
                HHLog.d(TAG, "files = $files")
            }
            emitter.onNext(files)
            emitter.onComplete()
        }
    }

    fun decodeVideo(
        context: Context,
        uri: Uri,
    ): Observable<HHFileModel> {
        return Observable.create { emitter ->
            val duration = getVideoDuration(context, uri)
            val fileType = HHFileType.imageFromGallery
            val filename = getFileName(context, uri) ?: "xxx.${GlobalConst.extMP4}"
            val image = getVideoThumbnail(context, uri)
            val rawData = getByteArray(context, uri)
            emitter.onNext(HHFileModel(fileType, image, filename, rawData, duration, null, null))
            emitter.onComplete()
        }
    }

    fun getMimeType(
        context: Context,
        uri: Uri,
    ): MimeType {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        if (mimeType?.startsWith("image/") == true) {
            return MimeType.image
        } else if (mimeType?.startsWith("video/") == true) {
            return MimeType.video
        }
        return MimeType.image
    }

    // Private Method
    // Uri를 통해 이미지 파일을 읽어 Bitmap으로 반환하는 메서드
    private fun getImageThumbnail(
        context: Context,
        uri: Uri?,
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri!!)

            // 이미지를 회전하기 위해 Matrix 객체 생성
            val matrix = Matrix()

            // BitmapFactory.Options를 사용하여 이미지의 원본 크기를 읽어옵니다.
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val reqWidth = 100 // 원하는 가로 크기
            val reqHeight = 100 // 원하는 세로 크기

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // BitmapFactory.Options를 다시 설정하여 비트맵을 생성합니다.
            val inputStreamAgain: InputStream = resolver.openInputStream(uri)!!
            bitmap = BitmapFactory.decodeStream(inputStreamAgain)
            inputStreamAgain.close()

            // 이미지를 원하는 방향으로 회전합니다.
            bitmap?.let {
                val rotation = getRotation(context, uri)
                matrix.postRotate(rotation.toFloat())
                bitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun getVideoThumbnail(
        context: Context,
        videoUri: Uri?,
    ): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoUri)
            return retriever.frameAtTime
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

    // Uri를 통해 데이터를 읽어 byte 배열로 반환하는 메서드
    private fun getByteArray(
        context: Context,
        uri: Uri,
    ): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            val resolver: ContentResolver = context.contentResolver
            val inputStream: InputStream? = resolver.openInputStream(uri)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOutputStream.toByteArray()
    }

    private fun getVideoDuration(
        context: Context,
        videoUri: Uri,
    ): Long {
        val resolver: ContentResolver = context.contentResolver
        var duration: Long = 0
        val projection = arrayOf(MediaStore.Video.Media.DURATION)
        resolver.query(videoUri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                duration = cursor.getLong(durationIndex)
            }
        }
        return duration
    }

    private fun getFileName(
        context: Context,
        uri: Uri,
    ): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }

    // 이미지의 회전 각도를 가져오는 함수
    private fun getRotation(
        context: Context,
        uri: Uri,
    ): Int {
        val exif =
            context.contentResolver.openInputStream(uri)?.use {
                android.media.ExifInterface(it)
            }
        return when (exif?.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL)) {
            android.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90
            android.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180
            android.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
