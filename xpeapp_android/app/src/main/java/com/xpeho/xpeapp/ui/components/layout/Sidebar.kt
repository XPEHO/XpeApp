package com.xpeho.xpeapp.ui.components.layout

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Resources
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingUiState
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.ClickyButton

@Composable
fun Sidebar(
    navigationController: NavController,
    sidebarVisible: MutableState<Boolean>
) {
    // Feature Flipping
    val context = LocalContext.current
    val ffViewModel = viewModel<FeatureFlippingViewModel>()
    LaunchedEffect(ffViewModel.uiState) {
        (ffViewModel.uiState as? FeatureFlippingUiState.ERROR)?.let {
            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
        }
    }

    // Computed sidebar size
    val sidebarWidth by animateFloatAsState(
        targetValue = if (sidebarVisible.value) 0.7f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 100,
            easing = { fraction -> fraction } // Linear easing
        )
    )

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(sidebarWidth)
            .background(color = XpehoColors.XPEHO_COLOR)
            // Prevent clickable element under the sidebar to be triggered on click
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {}
    ) {
        AnimatedVisibility(
            visible = sidebarVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 100))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 24.dp, bottom = 20.dp, start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    IconButton(
                        onClick = {
                            sidebarVisible.value = !sidebarVisible.value
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = XpehoRes.crossclose),
                            contentDescription = "Close Sidebar",
                            tint = Color.White,
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    SidebarItem(
                        navigationController = navigationController,
                        icon = painterResource(id = XpehoRes.anchor),
                        label = "Accueil",
                        route = Screens.Home.name
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    for (menuItem in Resources().listOfMenu) {
                        if (isEnabled(menuItem.featureFlippingId, ffViewModel.uiState)) {
                            SidebarItem(
                                navigationController = navigationController,
                                icon = painterResource(id = menuItem.idImage),
                                label = menuItem.title,
                                route = menuItem.redirection
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ClickyButton(
                        label = "Se déconnecter",
                        size = 18.sp,
                        verticalPadding = 5.dp,
                        horizontalPadding = 25.dp,
                        backgroundColor = Color.White,
                        labelColor = XpehoColors.CONTENT_COLOR
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            XpeApp.appModule.authenticationManager.logout()
                        }
                        // Return to login page and clear the backstack
                        navigationController.navigate(route = Screens.Login.name) {
                            popUpTo(Screens.Home.name) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

private fun isEnabled(feature: FeatureFlippingEnum, uiState: FeatureFlippingUiState): Boolean =
    uiState is FeatureFlippingUiState.SUCCESS && uiState.featureEnabled[feature] ?: false