package com.xpeho.xpeapp.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.xpeho.xpeapp.MainActivity
import com.xpeho.xpeapp.R

class Notification(private val context: Context) {

    companion object {
        const val NEWSLETTER_CHANNEL_ID = "XpeApp_Newsletters"
        const val NEWSLETTER_CHANNEL_NAME = "Nouvelles newsletters"
        const val NEWSLETTER_NOTIFICATION_TITLE = "Nouvelle newsletter !"
        const val NEWSLETTER_NOTIFICATION_MESSAGE = "Restez informé avec notre nouvelle newsletter !"

        const val QVST_NEW_CHANNEL_ID = "XpeApp_Qvst_New"
        const val QVST_NEW_CHANNEL_NAME = "Nouvelles campagnes QVST"
        const val QVST_NEW_NOTIFICATION_TITLE = "Nouvelle campagne QVST !"
        const val QVST_NEW_NOTIFICATION_MESSAGE = "Donnez votre avis dans la nouvelle campagne QVST !"

        const val QVST_REMINDER_CHANNEL_ID = "XpeApp_Qvst_Reminder"
        const val QVST_REMINDER_CHANNEL_NAME = "Rappels campagnes QVST"
        const val QVST_REMINDER_NOTIFICATION_TITLE = "Rappel campagne QVST !"
        const val QVST_REMINDER_NOTIFICATION_MESSAGE =
            "Dernier jour, n'oubliez pas de donner votre avis dans la campagne QVST !"

        const val QVST_RESULTS_CHANNEL_ID = "XpeApp_Qvst_Results"
        const val QVST_RESULTS_CHANNEL_NAME = "Résultats des campagnes QVST"
        const val QVST_RESULTS_NOTIFICATION_TITLE = "Résultats campagne QVST !"
        const val QVST_RESULTS_NOTIFICATION_MESSAGE = "Les résultats de la dernière campagne QVST sont disponibles !"
    }

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val newsletterChannel = NotificationChannel(
            NEWSLETTER_CHANNEL_ID,
            NEWSLETTER_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Être informé des nouvelles newsletters de XPEHO"
        }
        notificationManager.createNotificationChannel(newsletterChannel)

        val qvstNewChannel = NotificationChannel(
            QVST_NEW_CHANNEL_ID,
            QVST_NEW_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Être informé des nouvelles campagnes QVST de XPEHO"
        }
        notificationManager.createNotificationChannel(qvstNewChannel)

        val qvstReminderChannel = NotificationChannel(
            QVST_REMINDER_CHANNEL_ID,
            QVST_REMINDER_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avoir un rappel le dernier jour des campagnes QVST de XPEHO"
        }
        notificationManager.createNotificationChannel(qvstReminderChannel)

        val qvstResultChannel = NotificationChannel(
            QVST_RESULTS_CHANNEL_ID,
            QVST_RESULTS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Être informé de l'arrivée des résultats des campagnes QVST de XPEHO"
        }
        notificationManager.createNotificationChannel(qvstResultChannel)
    }


    fun sendNotification(channel: String, title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, channel)
            .setContentText(context.getString(R.string.app_name))
            .setContentTitle(title)
            .setSmallIcon(R.drawable.app_icon_cropped)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, notification)
    }

}