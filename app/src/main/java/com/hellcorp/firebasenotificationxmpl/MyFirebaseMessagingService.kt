package com.hellcorp.firebasenotificationxmpl

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        generateNotification()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //TODO
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun generateNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_beach_access_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(300, 300))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentText("Token recieved")
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder)
    }

    companion object {
        const val CHANNEL_ID = "notification_id"
        const val CHANNEL_NAME = "com.hellcorp.firebasenotificationxmpl"
    }
}