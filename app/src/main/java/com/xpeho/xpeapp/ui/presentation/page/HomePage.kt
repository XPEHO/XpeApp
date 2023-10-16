package com.xpeho.xpeapp.ui.presentation.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.ui.presentation.Resources
import com.xpeho.xpeapp.ui.presentation.componants.AppBar
import com.xpeho.xpeapp.ui.presentation.componants.Card
import com.xpeho.xpeapp.ui.presentation.viewModel.FeatureFlippingComposable
import com.xpeho.xpeapp.ui.presentation.viewModel.FeatureFlippingViewModel
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(
    vm: WordpressViewModel = viewModel(),
    navigationController: NavController,
    featureFlippingViewModel: FeatureFlippingViewModel,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.app_name),
                imageVector = Icons.AutoMirrored.Filled.Logout
            ) {
                vm.logout()
                navigationController.navigateUp()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            FeatureFlippingComposable(
                featureId = FeatureFlippingEnum.NEWSLETTERS.value,
                viewModel = featureFlippingViewModel,
                redirection = {
                    navigationController.navigate(route = "Newsletters")
                },
            ) {
                Card(
                    imageResource = R.drawable.newsletters,
                    title = "Newsletters",
                    color = colorResource(id = R.color.xpeho_color),
                )
            }
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 20.dp,
                ),
                columns = GridCells.Fixed(2),
                content = {
                    items(Resources().listOfMenu.dropWhile { it.idImage == R.drawable.newsletters }) { resource ->
                        FeatureFlippingComposable(
                            viewModel = featureFlippingViewModel,
                            featureId = resource.featureFlippingId.value,
                            redirection = {
                                navigationController.navigate(route = resource.redirection)
                            },
                        ) {
                            Card(
                                imageResource = resource.idImage,
                                title = resource.title,
                                color = setColor(resource.idImage),
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun setColor(idImage: Int): Color? {
    return if (idImage == R.drawable.expense_report || idImage == R.drawable.colleagues) {
        colorResource(id = R.color.xpeho_color)
    } else {
        null
    }
}
