package com.xpeho.xpeapp.ui.components

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import com.xpeho.xpeapp.MainActivity
import java.util.*

class NewsletterNotification {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(context: Context, content: String) {
        Log.i("NewsletterNotification", "showNotification")

        val notificationChannel = NotificationChannel(
            "newsletter_notification",
            "Newsletter",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        // Créer un Intent pour la redirection vers votre Compose
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            // Note(loucas): Not using FLAG_IMMUTABLE causes a crash on Android 12 and above
            // see: https://developer.android.com/about/versions/12/behavior-changes-12#pending-intent-mutability
            // see: https://github.com/XPEHO/XpeApp/issues/95
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(
            context,
            "newsletter_notification"
        )
            .setContentTitle("Une nouvelle newsletter est disponible !")
            .setContentText(content)
            .setSmallIcon(com.xpeho.xpeapp.R.drawable.newsletter_notification)
            .setContentIntent(pendingIntent) // Ajouter le PendingIntent pour la redirection
            .setAutoCancel(true) // Fermer la notification après la redirection
            .build()

        notificationManager.notify(
            Random().nextInt(),
            notification,
        )
    }
}
