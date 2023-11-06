package com.xpeho.xpeapp.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xpeho.xpeapp.presentation.page.HomePage
import com.xpeho.xpeapp.presentation.page.LoginPage
import com.xpeho.xpeapp.presentation.page.NewsletterPage
import com.xpeho.xpeapp.presentation.page.VacationPage
import com.xpeho.xpeapp.presentation.page.newsletter.detail.NewsletterDetailPage
import com.xpeho.xpeapp.presentation.viewModel.newsletter.detail.NewsletterDetailViewModel

enum class Screens {
    Login,
    Home,
    Newsletters,
    NewsletterDetail,
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
            LoginPage(
                onClick = {
                    navigationController.navigate(route = Screens.Home.name)
                }
            )
        }
        composable(route = Screens.Home.name) {
            HomePage(
                navigationController = navigationController,
                onBackPressed = {
                    navigationController.navigateUp()
                }
            )
        }
        composable(route = Screens.Newsletters.name) {
            NewsletterPage(
                navigationController = navigationController,
            ) {
                navigationController.navigateUp()
            }
        }
        composable(route = "${Screens.NewsletterDetail.name}/{newsletterId}") {
            val newsletterId = it.arguments?.getString("newsletterId") ?: ""
            NewsletterDetailPage(
                newsletterId = newsletterId,
            ) {
                navigationController.navigateUp()
            }
        }
        composable(route = Screens.Vacation.name) {
            VacationPage {
                navigationController.navigateUp()
            }
        }
    }
}