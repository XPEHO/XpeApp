package com.xpeho.xpeapp.ui.components

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.xpeho.xpeapp.ui.components.layout.Header
import com.xpeho.xpeapp.ui.components.layout.Sidebar

@Composable
fun Layout(
    navigationController: NavController,
    child: @Composable () -> Unit,
) {
    val sidebarVisible = remember {
        mutableStateOf(false)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Sidebar
        Box (
            modifier = Modifier
                .zIndex(10f)
        ) {
            AnimatedVisibility(
                visible = sidebarVisible.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200))
            ) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .fillMaxSize()
                        .alpha(0.3f)
                        .background(color = Color.Black)
                        .clickable {
                            sidebarVisible.value = !sidebarVisible.value
                        }
                )
            }
            Box(
                modifier = Modifier
                    .zIndex(3f)
            ) {
                Sidebar(
                    navigationController = navigationController,
                    sidebarVisible = sidebarVisible
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Header(sidebarVisible = sidebarVisible)
            child()
        }
    }
}
