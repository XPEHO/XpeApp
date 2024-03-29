package com.xpeho.xpeapp.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.page.ColleaguePage
import com.xpeho.xpeapp.ui.page.HomePage
import com.xpeho.xpeapp.ui.page.LoginPage
import com.xpeho.xpeapp.ui.page.VacationPage
import com.xpeho.xpeapp.ui.page.newsletter.NewsletterPage
import com.xpeho.xpeapp.ui.page.newsletter.detail.NewsletterDetailPage
import com.xpeho.xpeapp.ui.page.qvst.QvstCampaignDetailPage
import com.xpeho.xpeapp.ui.page.qvst.QvstPage

fun NavGraphBuilder.navigationBuilder(
    navigationController: NavHostController,
) {
    composable(route = Screens.Login.name) {
        LoginPage(
            onLoginSuccess = {
                navigationController.navigate(route = Screens.Home.name) {
                    popUpTo(Screens.Login.name) { inclusive = true }
                }
            }
        )
    }
    composable(route = Screens.Home.name) {
        HomePage(
            navigationController = navigationController,
            onDisconnectPressed = {
                // Return to login page and clear the backstack
                navigationController.navigate(route = Screens.Login.name) {
                    popUpTo(Screens.Home.name) { inclusive = true }
                }
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
    composable(route = Screens.Colleague.name) {
        ColleaguePage {
            navigationController.navigateUp()
        }
    }
    composable(route = Screens.Qvst.name) {
        QvstPage(
            navigationController = navigationController,
        ) {
            navigationController.navigateUp()
        }
    }
    composable(route = "${Screens.Qvst.name}/{campaignId}") {
        QvstCampaignDetailPage(
            qvstCampaignId = it.arguments?.getString("campaignId") ?: "",
            navController = navigationController,
        ) {
            navigationController.navigateUp()
        }
    }
}
