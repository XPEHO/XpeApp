package com.xpeho.xpeapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.presentation.page.HomePage
import com.xpeho.xpeapp.presentation.page.LoginPage
import com.xpeho.xpeapp.presentation.page.VacationPage

enum class Screens {
    Login,
    Home,
    Vacation,
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
            HomePage(
                navigationController = navigationController,
                onBackPressed = {
                    navigationController.navigateUp()
                }
            )
        }
        composable(route = Screens.Vacation.name) {
            VacationPage {
                navigationController.navigateUp()
            }
        }
    }
}