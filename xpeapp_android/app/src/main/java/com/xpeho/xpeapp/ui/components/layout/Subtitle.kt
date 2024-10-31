package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun Subtitle(
    label: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    Text(
        text = label,
        fontSize = 18.sp,
        fontFamily = XpehoFonts.raleway,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
    )
}