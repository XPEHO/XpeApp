package com.xpeho.xpeapp.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.enums.Screens

// Navigation animation duration in milliseconds.
// The value of 300 ms provides a more fluid navigation experience,
// giving the illusion of better performance.
private const val NAV_ANIM_DURATION_MILLIS = 300

@Composable
fun Home(startScreen: Screens) {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = startScreen.name,
        enterTransition = { fadeIn(animationSpec = tween(NAV_ANIM_DURATION_MILLIS)) },
        exitTransition = { fadeOut(animationSpec = tween(NAV_ANIM_DURATION_MILLIS)) }
    ) {
        navigationBuilder(navigationController)
    }
}
