package com.xpeho.xpeapp.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.Layout
import com.xpeho.xpeapp.ui.page.ColleaguePage
import com.xpeho.xpeapp.ui.page.HomePage
import com.xpeho.xpeapp.ui.page.LoginPage
import com.xpeho.xpeapp.ui.page.VacationPage
import com.xpeho.xpeapp.ui.page.NewsletterPage
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
        Layout(navigationController) {
            HomePage(
                navigationController = navigationController
            )
        }
    }
    composable(route = Screens.Newsletters.name) {
        Layout(navigationController) {
            NewsletterPage()
        }
    }
    composable(route = Screens.Vacation.name) {
        VacationPage()
    }
    composable(route = Screens.Colleague.name) {
        ColleaguePage {
            navigationController.navigateUp()
        }
    }
    composable(route = Screens.Qvst.name) {
        Layout(navigationController) {
            QvstPage(
                navigationController = navigationController,
            )
        }
    }
    composable(route = "${Screens.Qvst.name}/{campaignId}") {
        Layout(navigationController) {
            QvstCampaignDetailPage(
                qvstCampaignId = it.arguments?.getString("campaignId") ?: "",
                navController = navigationController,
            )
        }
    }
}
