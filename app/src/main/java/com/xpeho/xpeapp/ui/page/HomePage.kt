package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Resources
import com.xpeho.xpeapp.ui.componants.AppBar
import com.xpeho.xpeapp.ui.componants.ButtonElevated
import com.xpeho.xpeapp.ui.componants.Card
import com.xpeho.xpeapp.ui.theme.SfPro
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingComposable
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(
    vm: WordpressViewModel = viewModel(),
    navigationController: NavController,
    featureFlippingViewModel: FeatureFlippingViewModel,
) {
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.app_name),
                imageVector = Icons.AutoMirrored.Filled.Logout
            ) {
                showDialog.value = true
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            dialogDisconnection(
                showDialog,
            ) {
                vm.logout()
                // Return to login page and clear the backstack
                navigationController.navigate(
                    route = Screens.Login.name,
                ) {
                    popUpTo(Screens.Login.name) {
                        inclusive = true
                    }
                }
            }
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
fun dialogDisconnection(showDialog: MutableState<Boolean>, onBackPressed: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            containerColor = colorResource(id = R.color.xpeho_background_color),
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(
                    text = stringResource(id = R.string.home_disconnection),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        fontFamily = SfPro,
                        fontWeight = FontWeight.W400,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(60.dp)
                ) {
                    ButtonElevated(
                        text = "Oui",
                        backgroundColor = colorResource(id = R.color.colorPrimary),
                        textColor = Color.Black,
                    ) {
                        showDialog.value = false
                        onBackPressed()
                    }
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(60.dp)
                ) {
                    ButtonElevated(
                        text = "Non",
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                    ) {
                        showDialog.value = false
                    }
                }
            }
        )
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
