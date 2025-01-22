package com.xpeho.xpeapp.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.Layout
import com.xpeho.xpeapp.ui.components.layout.DisabledFeaturePlaceHolder
import com.xpeho.xpeapp.ui.page.HomePage
import com.xpeho.xpeapp.ui.page.LoginPage
import com.xpeho.xpeapp.ui.page.NewsletterPage
import com.xpeho.xpeapp.ui.page.qvst.QvstCampaignDetailPage
import com.xpeho.xpeapp.ui.page.qvst.QvstPage
import com.xpeho.xpeapp.ui.page.user.ProfilePage

fun NavGraphBuilder.navigationBuilder(
    navigationController: NavHostController,
) {
    val ffManager = XpeApp.appModule.featureFlippingManager

    navigationController.addOnDestinationChangedListener { _, _, _ ->
        ffManager.update()
    }

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
            if (ffManager.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)) {
                NewsletterPage()
            } else {
                DisabledFeaturePlaceHolder()
            }
        }
    }
    composable(route = Screens.Qvst.name) {
        Layout(navigationController) {
            if (ffManager.isFeatureEnabled(FeatureFlippingEnum.QVST)) {
                QvstPage(
                    navigationController = navigationController,
                )
            } else {
                DisabledFeaturePlaceHolder()
            }
        }
    }
    composable(route = "${Screens.Qvst.name}/{campaignId}") {
        Layout(navigationController) {
            if (ffManager.isFeatureEnabled(FeatureFlippingEnum.QVST)) {
                QvstCampaignDetailPage(
                    it.arguments?.getString("campaignId") ?: "", navigationController,
                )
            } else {
                DisabledFeaturePlaceHolder()
            }
        }
    }
    composable(route = Screens.Profile.name) {
        Layout(navigationController) {
            if (ffManager.isFeatureEnabled(FeatureFlippingEnum.PROFILE)) {
                ProfilePage(navigationController = navigationController)
            } else {
                DisabledFeaturePlaceHolder()
            }
        }
    }
}
