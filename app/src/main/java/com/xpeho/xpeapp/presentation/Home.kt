package com.xpeho.xpeapp.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.presentation.componants.AppLoader
import com.xpeho.xpeapp.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingUiState
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingViewModel

@Composable
fun Home(
    viewModel: FeatureFlippingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val navigationController = rememberNavController()
    viewModel.getFeatureFlipping()

    when (viewModel.uiState) {
        is FeatureFlippingUiState.LOADING -> AppLoader()
        is FeatureFlippingUiState.ERROR -> HomeError()
        is FeatureFlippingUiState.SUCCESS -> {
            NavHost(
                navController = navigationController,
                startDestination = Screens.Login.name,
            ) {
                navigationBuilder(navigationController, viewModel)
            }
        }
    }
}

@Composable
fun HomeError(
    viewModel: FeatureFlippingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val error = (viewModel.uiState as FeatureFlippingUiState.ERROR).error
    Log.e("Home", "Error: $error")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(id = com.xpeho.xpeapp.R.string.home_page_error))
        Box(modifier = Modifier.height(16.dp))
        ButtonElevated(
            text = stringResource(id = com.xpeho.xpeapp.R.string.home_page_reload),
            backgroundColor = colorResource(id = com.xpeho.xpeapp.R.color.xpeho_color),
            textColor = Color.White,
            icon = null,
        ) {
            viewModel.getFeatureFlipping()
        }
    }
}
