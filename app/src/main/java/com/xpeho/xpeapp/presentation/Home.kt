package com.xpeho.xpeapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.presentation.page.HomePage
import com.xpeho.xpeapp.presentation.page.LoginPage

enum class Screens {
    Login,
    Home,
}

@Composable
fun Home() {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = Screens.Login.name,
    ) {
        composable(route = Screens.Login.name) {
            LoginPage(onClick = {
                navigationController.navigate(route = Screens.Home.name)
            })
        }
        composable(route = Screens.Home.name) {
            HomePage(onBackPressed = {
                navigationController.navigateUp()
            })
        }
    }
}