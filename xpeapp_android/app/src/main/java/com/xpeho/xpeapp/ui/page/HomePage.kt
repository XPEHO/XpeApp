package com.xpeho.xpeapp.ui.page

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.xpeho.xpeapp.ui.Resources
import com.xpeho.xpeapp.ui.componants.AppBar
import com.xpeho.xpeapp.ui.componants.ButtonElevated
import com.xpeho.xpeapp.ui.componants.Card
import com.xpeho.xpeapp.ui.componants.qvst.QvstBreadcrumb
import com.xpeho.xpeapp.ui.modifier.greyScale
import com.xpeho.xpeapp.ui.theme.SfPro
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingUiState
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(onDisconnectPressed : () -> Unit, navigationController: NavController) {
    val ffViewModel = viewModel<FeatureFlippingViewModel>()
    val context = LocalContext.current
    LaunchedEffect(ffViewModel.uiState){
        (ffViewModel.uiState as? FeatureFlippingUiState.ERROR)?.let {
            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
        }
    }
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBarContent(ffViewModel.uiState ,navigationController, showDialog)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 0.dp
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            dialogDisconnection(
                showDialog,
                onDisconnectPressed
            )
            PageGrid(ffViewModel.uiState, navigationController)
        }
    }
}

private const val DISABLED_ALPHA = 0.4f
private const val ENABLED_ALPHA = 1f
private const val DISABLED_SATURATION = 0f
private const val ENABLED_SATURATION = 1f
@Composable
private fun ToggleableCard(title: String, imageResource: Int, color: Color?, isEnabled: Boolean,
    onClick: () -> Unit) {
    val alpha by animateFloatAsState(if (isEnabled) ENABLED_ALPHA else DISABLED_ALPHA,
        label="alpha")
    val saturation by animateFloatAsState(if (isEnabled) ENABLED_SATURATION else DISABLED_SATURATION,
        label="saturation")
    Card(
        modifier = Modifier
            .greyScale(saturation)
            .alpha(alpha)
            .clickable {
                if (isEnabled)
                    onClick()
            },
        imageResource = imageResource,
        title = title,
        color = color,
    )
}

@Composable
private fun PageGrid(uiState: FeatureFlippingUiState, navigationController: NavController) {
    ToggleableCard(
        title = "Newsletters",
        imageResource = R.drawable.newsletters,
        color = colorResource(id = R.color.xpeho_color),
        isEnabled = isEnabled(FeatureFlippingEnum.NEWSLETTERS, uiState),
        onClick = { navigationController.navigate(route = "Newsletters") }
    )
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 20.dp,
        ),
        columns = GridCells.Fixed(2),
        content = {
            items(Resources().listOfMenu.dropWhile { it.idImage == R.drawable.newsletters }) {
                ToggleableCard(
                    title = it.title,
                    imageResource = it.idImage,
                    color = setColor(it.idImage),
                    isEnabled = isEnabled(it.featureFlippingId, uiState),
                    onClick = { navigationController.navigate(route = it.redirection) }
                )
            }
        }
    )
}

@Composable
private fun TopBarContent(uiState: FeatureFlippingUiState, navigationController: NavController,
    showDialog: MutableState<Boolean>) {
    Column{
        AppBar(
            title = stringResource(id = R.string.app_name),
            imageVector = Icons.AutoMirrored.Filled.Logout,
            actions = {
                val isEnabled = isEnabled(FeatureFlippingEnum.QVST, uiState)
                AnimatedVisibility(isEnabled) {
                    Image(
                        painter = painterResource(id = R.drawable.qvst),
                        contentDescription = null,
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .padding(16.dp)
                            .clickable {
                                if (isEnabled)
                                    navigationController.navigate(route = "QVST")
                            }
                    )
                }
            },
        ) {
            showDialog.value = true
        }
        val isEnabled = isEnabled(FeatureFlippingEnum.QVST, uiState)
        AnimatedVisibility(isEnabled) {
            QvstBreadcrumb(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isEnabled) navigationController.navigate(route = "QVST")
                }
                .padding(horizontal = 16.dp)
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

private fun isEnabled(feature: FeatureFlippingEnum, uiState: FeatureFlippingUiState): Boolean =
    uiState is FeatureFlippingUiState.SUCCESS && uiState.featureEnabled[feature] ?: false
