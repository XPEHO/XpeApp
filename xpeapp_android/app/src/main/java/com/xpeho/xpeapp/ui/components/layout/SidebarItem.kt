package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun SidebarItem(
    icon: Painter,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = icon,
            contentDescription = "$label Icon",
            tint = Color.White,
            modifier = Modifier
                .size(22.dp)
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Subtitle(label = label)
    }
}