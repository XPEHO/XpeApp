package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.Measurements
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun Header(sidebarVisible: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .zIndex(Measurements.HEADER_ELEVATION)
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        IconButton(
            onClick = {
                sidebarVisible.value = !sidebarVisible.value
            }
        ) {
            Icon(
                painter = painterResource(id = XpehoRes.burgermenu),
                contentDescription = "Open Sidebar",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier
                    .padding(6.dp)
                    .size(32.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.app_icon_without_bg),
            contentDescription = "App Icon",
            tint = XpehoColors.XPEHO_COLOR,
            modifier = Modifier
                .scale(Measurements.APP_ICON_SCALE)
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