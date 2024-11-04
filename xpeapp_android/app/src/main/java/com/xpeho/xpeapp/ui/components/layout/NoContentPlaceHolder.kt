package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun NoContentPlaceHolder() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.NotInterested,
                contentDescription = "No content icon",
                tint = XpehoColors.XPEHO_DARK_COLOR,
            )
            Text(
                "Rien à signaler ici !",
                color = XpehoColors.XPEHO_DARK_COLOR
            )
        }
    }
}