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
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel

fun NavGraphBuilder.navigationBuilder(
    navigationController: NavHostController,
    viewModel: FeatureFlippingViewModel
) {
    composable(route = Screens.Login.name) {
        LoginPage(
            featureFlippingViewModel = viewModel,
        ) {
            navigationController.navigate(route = Screens.Home.name)
        }
    }
    composable(route = Screens.Home.name) {
        HomePage(
            navigationController = navigationController,
            featureFlippingViewModel = viewModel,
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
}
