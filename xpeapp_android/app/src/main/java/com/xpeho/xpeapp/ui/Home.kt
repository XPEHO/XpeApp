package com.xpeho.xpeapp.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.xpeho.xpeapp.enums.Screens

@Composable
fun Home(startScreen: Screens) {
    FirebaseMessaging.getInstance().subscribeToTopic("newsletter")
    FirebaseMessaging.getInstance()
        .token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Home", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("Home", "Token: $token")
        }
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = startScreen.name,
    ) {
        navigationBuilder(navigationController)
    }
}
