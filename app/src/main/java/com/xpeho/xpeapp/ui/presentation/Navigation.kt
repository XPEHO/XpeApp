package com.xpeho.xpeapp.ui.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.presentation.page.ColleaguePage
import com.xpeho.xpeapp.presentation.page.VacationPage
import com.xpeho.xpeapp.ui.presentation.page.newsletter.NewsletterPage
import com.xpeho.xpeapp.ui.presentation.page.newsletter.detail.NewsletterDetailPage
import com.xpeho.xpeapp.ui.presentation.viewModel.FeatureFlippingViewModel
import com.xpeho.xpeapp.ui.presentation.page.HomePage
import com.xpeho.xpeapp.ui.presentation.page.LoginPage

fun NavGraphBuilder.navigationBuilder(
    navigationController: NavHostController,
    viewModel: FeatureFlippingViewModel
) {
    composable(route = Screens.Login.name) {
        LoginPage {
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
