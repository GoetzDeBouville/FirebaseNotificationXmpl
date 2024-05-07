package com.hellcorp.firebasenotificationxmpl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var token = ""
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.notification != null) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                token = task.result
            })

            generateNotification(
                "message.notification!!.title!!",
                "message.notification!!.body!!"
            )
        }
    }

    private fun generateNotification(title: String, message: String) {
        val sharedPreferences =
            applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_beach_access_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(300, 300))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentText("Token: $token")
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

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews =
            RemoteViews("com.hellcorp.firebasenotificationxmpl", R.layout.notification)

        remoteViews.apply {
            setTextViewText(R.id.tv_title, title)
            setTextViewText(R.id.tv_description, message)
            setImageViewResource(R.id.iv_icon, R.drawable.baseline_beach_access_24)
        }

        return remoteViews
    }

    private fun saveToken(token: String) {
        val sharedPreferences =
            applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", token).apply()
    }


    companion object {
        const val CHANNEL_ID = "notification_id"
        const val CHANNEL_NAME = "com.hellcorp.firebasenotificationxmpl"
    }
}