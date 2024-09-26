package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.xpeho.xpeapp.R
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

@Composable
fun Header(sidebarVisible: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .zIndex(10f)
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        IconButton(
            onClick = {
                sidebarVisible.value = !sidebarVisible.value
            }
        ) {
            Icon(
                painter = painterResource(id = XpehoRes.burgermenu), // TODO: Replace with theme
                contentDescription = "Open Sidebar",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier
                    .padding(6.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.app_icon_without_bg),
            contentDescription = "App Icon",
            tint = XpehoColors.XPEHO_COLOR,
            modifier = Modifier
                .scale(2.8F)
                .padding(end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    val sidebarVisible = remember { mutableStateOf(false) } // Replace with your resource

    Header(sidebarVisible)
}