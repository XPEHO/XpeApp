package com.xpeho.xpeapp.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val SP_NOTIFICATIONS_KEY = "xpeapp_notification_checks"
        const val SP_LAST_KNOWN_NEWSLETTER_KEY = "lastKnownNewsletter"
        const val SP_LAST_KNOWN_OPEN_CAMPAIGN_KEY = "lastKnownOpenCampaign"
        const val SP_LAST_KNOWN_CAMPAIGN_RESULT_KEY = "lastKnownCampaignResult"
        const val NOTIFICATION_INTERVAL_MILLIS = 1000L
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm received!!")
        val scheduleNotificationService = context?.let { Notification(it) }

        // Check if today is a working day (not the week-end)
        val today = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        if (today == Calendar.SATURDAY || today == Calendar.SUNDAY) {
            return
        }

        context?.let { ctx ->
            // Init local storage with encrypted shared preferences
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                SP_NOTIFICATIONS_KEY,
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Get modules for DI
            val appModule = XpeApp.appModule
            val datastorePref = appModule.datastorePref
            val wordpressRepo = appModule.wordpressRepository
            val firebaseService = appModule.firebaseService

            CoroutineScope(Dispatchers.IO).launch {
                // Get auth data
                val authData = datastorePref.getAuthData()
                Log.d("AlarmReceiver", "authData: $authData")

                // Check if there are new newsletters
                checkForNewNewsletters(
                    firebaseService,
                    sharedPreferences,
                    scheduleNotificationService
                )
                delay(NOTIFICATION_INTERVAL_MILLIS)

                // Check if there are new campaigns
                checkForNewCampaigns(
                    authData,
                    wordpressRepo,
                    sharedPreferences,
                    scheduleNotificationService
                )
                delay(NOTIFICATION_INTERVAL_MILLIS)

                // Check if there are campaigns that end today
                checkForCampaignsEndingToday(
                    authData,
                    wordpressRepo,
                    scheduleNotificationService
                )
                delay(NOTIFICATION_INTERVAL_MILLIS)

                // Check if there are qvst results available
                checkForQvstResults(
                    authData,
                    wordpressRepo,
                    sharedPreferences,
                    scheduleNotificationService
                )
            }
        }
    }

    private suspend fun checkForNewNewsletters(
        firebaseService: FirebaseService,
        sharedPreferences: SharedPreferences,
        scheduleNotificationService: Notification?
    ) {
        // Get the last known newsletter
        val lastKnownNewsletter = sharedPreferences.getString(SP_LAST_KNOWN_NEWSLETTER_KEY, null)
        // Get the last published newsletter
        val newsletters = firebaseService.fetchNewsletters()
        val lastNewsletter = newsletters.firstOrNull()

        lastNewsletter?.let { newsletter ->
            // Save the last published newsletter
            val lastPublishedNewsletter = newsletter.pdfUrl
            sharedPreferences.edit().putString(SP_LAST_KNOWN_NEWSLETTER_KEY, lastPublishedNewsletter).apply()

            // If we have a known newsletter and the last published newsletter is different than this one,
            // we have a new newsletter
            // Else we save and empty string to trigger the notification next time
            if (lastKnownNewsletter != null
                && lastPublishedNewsletter.isNotEmpty()
                && lastPublishedNewsletter != lastKnownNewsletter
            ) {
                scheduleNotificationService?.sendNotification(
                    Notification.NEWSLETTER_CHANNEL_ID,
                    Notification.NEWSLETTER_NOTIFICATION_TITLE,
                    Notification.NEWSLETTER_NOTIFICATION_MESSAGE
                )
            }
        } ?: sharedPreferences.edit().putString(SP_LAST_KNOWN_NEWSLETTER_KEY, "").apply()
    }

    private suspend fun checkForNewCampaigns(
        authData: AuthData?,
        wordpressRepo: WordpressRepository,
        sharedPreferences: SharedPreferences,
        scheduleNotificationService: Notification?
    ) {
        // Get the last known open campaign
        val lastKnownOpenCampaign = sharedPreferences.getString(SP_LAST_KNOWN_OPEN_CAMPAIGN_KEY, null)
        // If we have an authData, we can check for new open campaigns
        authData?.let {
            // Get the last published open campaign
            val openCampaigns = wordpressRepo.getQvstCampaigns(username = authData.username, onlyActive = true)
                ?.sortedByDescending { campaign -> campaign.id }
            val lastOpenCampaign = openCampaigns?.firstOrNull()

            lastOpenCampaign?.let { campaign ->
                // Save the last published open campaign
                val lastPublishedOpenCampaign = campaign.id
                sharedPreferences.edit().putString(SP_LAST_KNOWN_OPEN_CAMPAIGN_KEY, lastPublishedOpenCampaign).apply()

                // If we know a campaign and the last published open campaign is different than this one,
                // we have a new campaign
                // Else we save and empty string to trigger the notification next time
                if (lastKnownOpenCampaign != null
                    && lastPublishedOpenCampaign.isNotEmpty()
                    && lastPublishedOpenCampaign != lastKnownOpenCampaign
                ) {
                    scheduleNotificationService?.sendNotification(
                        Notification.QVST_NEW_CHANNEL_ID,
                        Notification.QVST_NEW_NOTIFICATION_TITLE,
                        Notification.QVST_NEW_NOTIFICATION_MESSAGE
                    )
                }
            } ?: sharedPreferences.edit().putString(SP_LAST_KNOWN_OPEN_CAMPAIGN_KEY, "").apply()
        }
    }

    private suspend fun checkForCampaignsEndingToday(
        authData: AuthData?,
        wordpressRepo: WordpressRepository,
        scheduleNotificationService: Notification?
    ) {
        // If we have an authData, we can check for campaigns ending today
        authData?.let {
            // See if there are open campaigns ending today
            val openCampaigns = wordpressRepo.getQvstCampaigns(username = authData.username, onlyActive = true)
            val campaignThatEndToday = openCampaigns?.firstOrNull { campaign -> campaign.remainingDays == 0 }

            // If there is a campaign that ends today, we send a notification
            campaignThatEndToday?.let {
                scheduleNotificationService?.sendNotification(
                    Notification.QVST_REMINDER_CHANNEL_ID,
                    Notification.QVST_REMINDER_NOTIFICATION_TITLE,
                    Notification.QVST_REMINDER_NOTIFICATION_MESSAGE
                )
            }
        }
    }

    private suspend fun checkForQvstResults(
        authData: AuthData?,
        wordpressRepo: WordpressRepository,
        sharedPreferences: SharedPreferences,
        scheduleNotificationService: Notification?
    ) {
        // Get the last known campaign result
        val lastKnownCampaignResult = sharedPreferences.getString(SP_LAST_KNOWN_CAMPAIGN_RESULT_KEY, null)

        // If we have an authData, we can check for new qvst results
        authData?.let {
            // Get the last published campaign result
            val openCampaigns = wordpressRepo.getQvstCampaigns(username = authData.username)
            val lastCampaignWithResult = openCampaigns?.firstOrNull { campaign -> campaign.resultLink.isNotEmpty() }

            lastCampaignWithResult?.let { campaign ->
                // Save the last published campaign result
                val lastPublishedCampaignResult = campaign.resultLink
                sharedPreferences.edit().putString(SP_LAST_KNOWN_CAMPAIGN_RESULT_KEY, lastPublishedCampaignResult)
                    .apply()

                // If we know a campaign result and the last published campaign result is different than this one,
                // we have a new result
                // Else we save and empty string to trigger the notification next time
                if (lastKnownCampaignResult != null
                    && lastPublishedCampaignResult.isNotEmpty()
                    && lastPublishedCampaignResult != lastKnownCampaignResult
                ) {
                    scheduleNotificationService?.sendNotification(
                        Notification.QVST_RESULTS_CHANNEL_ID,
                        Notification.QVST_RESULTS_NOTIFICATION_TITLE,
                        Notification.QVST_RESULTS_NOTIFICATION_MESSAGE
                    )
                }
            } ?: sharedPreferences.edit().putString(SP_LAST_KNOWN_CAMPAIGN_RESULT_KEY, "").apply()
        }
    }
}