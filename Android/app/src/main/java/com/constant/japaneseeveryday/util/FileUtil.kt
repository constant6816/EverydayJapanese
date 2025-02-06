package com.constant.japaneseeveryday.util

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileUtil {
    fun unzipAndSaveFiles(
        context: Context,
        inputStream: InputStream,
    ) {
        ZipInputStream(inputStream).use { zipInputStream ->
            var entry: ZipEntry?
            while (zipInputStream.nextEntry.also { entry = it } != null) {
                entry?.let {
                    when {
                        it.isDirectory -> {
                            // 디렉토리 무시
                        }
                        it.name.endsWith(".jpg", true) || it.name.endsWith(".jpeg", true) ||
                            it.name.endsWith(".png", true) -> {
                            saveMediaToGallery(context, zipInputStream, it.name, "image")
                        }
                        it.name.endsWith(".mp4", true) || it.name.endsWith(".mov", true) -> {
                            saveMediaToGallery(context, zipInputStream, it.name, "video")
                        }
                    }
                }
            }
        }
    }

    fun saveMediaToGallery(
        context: Context,
        inputStream: InputStream,
        fileName: String,
        mediaType: String,
    ) {
        val contentValues =
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                when (mediaType) {
                    "image" -> {
                        put(
                            MediaStore.MediaColumns.MIME_TYPE,
                            when {
                                fileName.endsWith(".jpg", true) || fileName.endsWith(".jpeg", true) -> "image/jpeg"
                                fileName.endsWith(".png", true) -> "image/png"
                                else -> "image/*"
                            },
                        )
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    "video" -> {
                        put(
                            MediaStore.MediaColumns.MIME_TYPE,
                            when {
                                fileName.endsWith(".mp4", true) -> "video/mp4"
                                fileName.endsWith(".mov", true) -> "video/quicktime"
                                else -> "video/*"
                            },
                        )
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
                    }
                }
            }

        val uri =
            when (mediaType) {
                "image" -> context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                "video" -> context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                else -> null
            }

        uri?.let {
            context.contentResolver.openOutputStream(it).use { outputStream ->
                inputStream.copyTo(outputStream!!)
            }
        }
    }
}
