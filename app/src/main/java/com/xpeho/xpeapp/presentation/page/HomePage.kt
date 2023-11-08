package com.xpeho.xpeapp.presentation.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.presentation.Resources
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.Card
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingComposable
import com.xpeho.xpeapp.presentation.viewModel.FeatureFlippingViewModel

@Composable
fun HomePage(
    onBackPressed: () -> Unit,
    navigationController: NavController,
    featureFlippingViewModel: FeatureFlippingViewModel,
) {
    Column {
        AppBar(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            title = null,
        ) {
            onBackPressed()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            FeatureFlippingComposable(
                featureId = FeatureFlippingEnum.NEWSLETTERS.value,
                viewModel = featureFlippingViewModel,
            ) {
                Card(
                    imageResource = R.drawable.newsletters,
                    title = "Newsletters",
                    color = colorResource(id = R.color.xpeho_color),
                    redirection = {
                        navigationController.navigate(route = "Newsletters")
                    },
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
                        ) {
                            Card(
                                imageResource = resource.idImage,
                                title = resource.title,
                                color = setColor(resource.idImage),
                                redirection = {
                                    navigationController.navigate(route = resource.redirection)
                                },
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
