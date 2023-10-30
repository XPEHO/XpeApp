package com.xpeho.xpeapp.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.presentation.page.ColleaguePage
import com.xpeho.xpeapp.presentation.page.HomePage
import com.xpeho.xpeapp.presentation.page.LoginPage
import com.xpeho.xpeapp.presentation.page.NewsletterPage
import com.xpeho.xpeapp.presentation.page.VacationPage
import com.xpeho.xpeapp.presentation.page.newsletter.detail.NewsletterDetailPage
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingUiState
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingViewModel

enum class Screens {
    Login,
    Home,
    Newsletters,
    NewsletterDetail,
    Vacation,
    Colleague,
}

@Composable
fun Home(
    viewModel: FeatureFlippingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val navigationController = rememberNavController()
    viewModel.getFeatureFlipping()

    when (viewModel.uiState) {
        is FeatureFlippingUiState.LOADING -> {
            Log.d("Home", "Loading")
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        }
        is FeatureFlippingUiState.ERROR -> {
            val error = (viewModel.uiState as FeatureFlippingUiState.ERROR).error
            Log.e("Home", "Error: $error")
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Une erreur est survenue !")
                Box(modifier = Modifier.height(16.dp))
                ButtonElevated(
                    text = "Recharger la page",
                    backgroundColor = colorResource(id = com.xpeho.xpeapp.R.color.xpeho_color),
                    textColor = Color.White,
                    icon = null,
                ) {
                    viewModel.getFeatureFlipping()
                }
            }
        }
        is FeatureFlippingUiState.SUCCESS -> {
            Log.d("Home", "Success")
            Log.d("Home", "Features: ${viewModel.featuresState.value}")
            NavHost(
                navController = navigationController,
                startDestination = Screens.Login.name,
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
                composable(route = Screens.Colleague.name) {
                    ColleaguePage {
                        navigationController.navigateUp()
                    }
                }
            }
        }
    }
}