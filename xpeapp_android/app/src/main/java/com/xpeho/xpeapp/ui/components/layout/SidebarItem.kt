package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun SidebarItem(
    navigationController: NavController,
    icon: Painter,
    label: String,
    route: String
) {
    Row(
        modifier = Modifier
            .clickable {
                navigationController.navigate(route = route)
            }
    ) {
        Icon(
            painter = icon,
            contentDescription = "$label Icon",
            tint= Color.White,
            modifier = Modifier
                .size(22.dp)
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Text(
            label,
            fontSize = 18.sp,
            fontFamily = XpehoFonts.raleway,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}