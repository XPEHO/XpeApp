package com.xpeho.xpeapp.ui

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.xpeho.xpeapp.enums.Screens

// Navigation animation duration in milliseconds.
// The value of 300 ms provides a more fluid navigation experience,
// giving the illusion of better performance.
private const val NAV_ANIM_DURATION_MILLIS = 300

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
        enterTransition = {fadeIn(animationSpec = tween(NAV_ANIM_DURATION_MILLIS))},
        exitTransition = {fadeOut(animationSpec = tween(NAV_ANIM_DURATION_MILLIS))}
    ) {
        navigationBuilder(navigationController)
    }
}
