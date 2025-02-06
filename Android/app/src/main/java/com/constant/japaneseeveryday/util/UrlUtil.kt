package com.constant.japaneseeveryday.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

object UrlUtil {
    fun getImageBitmap(
        context: Context,
        imageUrl: String,
    ): Bitmap? {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .submit()
                .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        } catch (e: ExecutionException) {
            e.printStackTrace()
            null
        }
    }

    fun getVideoThumbnail(videoUrl: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoUrl)
            return retriever.frameAtTime
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

    fun getVideoDuration(videoUrl: String): Long? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoUrl, HashMap<String, String>())
            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            return durationString?.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getVideoDuration(
        videoUrl: String,
        callback: (Long?) -> Unit,
    ) {
        val retriever = MediaMetadataRetriever()
        val executor = Executors.newSingleThreadExecutor()
        val mainHandler = Handler(Looper.getMainLooper())
        executor.execute {
            var duration: Long? = null
            try {
                retriever.setDataSource(videoUrl, HashMap<String, String>())
                val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                duration = durationString?.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    retriever.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            mainHandler.post {
                callback(duration)
            }
        }
        executor.shutdown()
    }
}
