package com.xpeho.xpeapp.data.service

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.ui.components.NewsletterNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(token)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val context = this
        GlobalScope.launch {
            remoteMessage.notification?.let {
                val isConnected = DatastorePref(
                    context = context
                ).isConnectedLeastOneTime
                if (isConnected.first()) {
                    try {
                        NewsletterNotification().showNotification(
                            context = context,
                            content = it.body.toString(),
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error while showing notification", e)
                        Toast.makeText(context, "Error while showing notification: ${e.message}",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}