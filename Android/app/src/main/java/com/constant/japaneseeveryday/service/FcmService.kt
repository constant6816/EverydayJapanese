package com.constant.japaneseeveryday.service

import android.content.Intent
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.util.ActivityUtil
import com.constant.japaneseeveryday.util.HHIntent
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.LinkConst
import com.constant.japaneseeveryday.util.NotificationUtil
import com.constant.japaneseeveryday.util.nonNull
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    private val TAG = nonNull(this::class.simpleName)

    // Notification, Data 는 Android 용이고,
    // apns 는 iOS 용이다.
    // 안드로이드는 Foreground 일 경우에만 FcmService의 onMessageReceived() 함수가 호출되고,
    // handleIntent() 함수는 Foreground, background 모두 호출된다.
    // Notification 의 경우는 앱이 백그라운드 일 경우에 onMessageReceived() 함수도 호출되지 않은채 Noti를 무조건 보여준다.
    // 그래서 notification 은 안 보내고, Data 만 보내고, handleIntent 에서 처리하게 해 두었다.

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        HHLog.d(TAG, "handleIntent() intent = $intent")

        intent?.extras?.let {
            val remoteMessage = RemoteMessage(it)
            HHLog.d(TAG, "handleIntent() remoteMessage = $remoteMessage")

            // 앱이 실행중이라면

        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        HHLog.d(TAG, "onMessageReceived()")
        HHLog.d(TAG, "message = $message")
        HHLog.d(TAG, "message.data.notiType = ${message.data.get("notiType")}")
        HHLog.d(TAG, "message.notification = ${message.notification}")
        HHLog.d(TAG, "message.notification.title = ${message.notification?.title}")
        HHLog.d(TAG, "message.notification.body = ${message.notification?.body}")
    }
}
