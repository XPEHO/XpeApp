package com.xpeho.xpeapp.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AlarmScheduler {

    companion object {
        private const val ID = 12345
        private const val INTERVAL_MILLIS = AlarmManager.INTERVAL_DAY
        private val ALARM_TIME = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    fun scheduleAlarm(
        context: Context
    ) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            ALARM_TIME.timeInMillis,
            INTERVAL_MILLIS,
            pendingIntent
        )
    }

}