package com.xpeho.xpeapp.ui.presentation.componants

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.util.Log
import java.util.*

class NewsletterNotification {

    fun showNotification(context: Context, content: String) {

        Log.i("NewsletterNotification", "showNotification")

        val notificationChannel = NotificationChannel(
            "newsletter_notification",
            "Newsletter",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        val notification = Notification.Builder(
            context,
            "newsletter_notification"
        )
            .setContentTitle("Une nouvelle newsletter est disponible !")
            .setContentText(content)
            .setSmallIcon(com.xpeho.xpeapp.R.drawable.newsletter_notification)
            .build()

        notificationManager.notify(
            Random().nextInt(),
            notification,
        )
    }
}