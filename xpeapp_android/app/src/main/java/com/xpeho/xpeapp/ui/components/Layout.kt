package com.xpeho.xpeapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.xpeho.xpeapp.ui.Measurements
import com.xpeho.xpeapp.ui.components.layout.Header
import com.xpeho.xpeapp.ui.components.layout.Sidebar
import com.xpeho.xpeapp.ui.page.about.AboutView

@Composable
fun Layout(
    navigationController: NavController,
    child: @Composable () -> Unit,
) {
    val sidebarVisible = remember {
        mutableStateOf(false)
    }
    val showDialog = remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Sidebar
        Box(
            modifier = Modifier
                .zIndex(Measurements.SIDEBAR_ELEVATION)
        ) {
            AnimatedVisibility(
                visible = sidebarVisible.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200))
            ) {
                Box(
                    modifier = Modifier
                        .zIndex(Measurements.SIDEBAR_BACKGROUND_ELEVATION)
                        .fillMaxSize()
                        .alpha(Measurements.SIDEBAR_BACKGROUND_OPACITY)
                        .background(color = Color.Black)
                        .clickable {
                            sidebarVisible.value = !sidebarVisible.value
                        }
                )
            }
            Box(
                modifier = Modifier
                    .zIndex(Measurements.SIDEBAR_MENU_ELEVATION)
            ) {
                Sidebar(
                    navigationController = navigationController,
                    sidebarVisible = sidebarVisible,
                    showDialog = showDialog
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(sidebarVisible = sidebarVisible)
            child()
        }
    }
    if (showDialog.value) {
        AboutView(onDismiss = { showDialog.value = false }, context = LocalContext.current)
    }
}
