package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xpeho.xpeho_ui_android.foundations.Colors
import com.xpeho.xpeho_ui_android.foundations.Fonts

@Composable
fun ListFilterTitle(
    label: String
) {
    Text(
        text = label,
        fontSize = 16.sp,
        fontFamily = Fonts.rubik,
        fontWeight = FontWeight.SemiBold,
        color = Colors.CONTENT_COLOR
    )
}