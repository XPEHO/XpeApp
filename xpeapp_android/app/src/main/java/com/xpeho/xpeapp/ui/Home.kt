package com.xpeho.xpeapp.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel

@Composable
fun Home(
    viewModel: FeatureFlippingViewModel = viewModel(),
) {
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
        startDestination = Screens.Login.name,
    ) {
        navigationBuilder(navigationController, viewModel)
    }
}
