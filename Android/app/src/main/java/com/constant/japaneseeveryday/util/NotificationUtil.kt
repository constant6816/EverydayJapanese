package com.constant.japaneseeveryday.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.scene.main.MainActivity

object NotificationUtil {
    private val TAG = nonNull(this::class.simpleName)

    private var currentId = 0

    fun generateId(): Int {
        return ++currentId
    }

    fun makeNotification(
        context: Context,
        title: String?,
        body: String?,
        groupId: Int?,
        notiType: String,
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(context, "channelId")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)

        val pendingIntent = createPendingIntent(context, groupId, notiType)
        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // notification 여러개 쌓을거면 id를 각각 다른 값으로 생성 / 갱신할거면 같은 id 사용 , pendingIntent request code도 함께 설정 해줘야함
        notificationManager.notify(generateId(), notificationBuilder.build())
    }

    fun createPendingIntent(
        context: Context,
        groupId: Int?,
        notiType: String,
    ): PendingIntent? {
        if (notiType.isNotEmpty()) {
            val intent =
                Intent(
                    context,
                    MainActivity::class.java,
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            when (notiType) {
                LinkConst.CLUB_CHAT -> {
                    intent.putExtra(HHIntent.EXTRA_GROUP_ID, groupId)
                    intent.putExtra(HHIntent.EXTRA_CALL_BACK_URI, LinkConst.CLUB_CHAT)
                    HHLog.d(TAG, "createPendingIntent() putExtra()")
                }
                else -> {
                }
            }

            val pendingIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                } else {
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

            return pendingIntent
        } else {
            return null
        }
    }
}
