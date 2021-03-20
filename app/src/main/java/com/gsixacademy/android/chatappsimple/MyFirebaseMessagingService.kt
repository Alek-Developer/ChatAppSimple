package com.gsixacademy.android.chatappsimple

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "MyFirebaseMessagingService"
    }

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "FCM Message Id: ${remoteMessage!!.messageId}")
        Log.d(TAG, "FCM Notification Message: ${remoteMessage.notification}")
        Log.d(TAG, "FCM Data Message: ${remoteMessage.data}")



    }
}