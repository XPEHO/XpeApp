package com.xpeho.xpeapp.ui.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.presentation.componants.AppLoader
import com.xpeho.xpeapp.ui.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingUiState
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
