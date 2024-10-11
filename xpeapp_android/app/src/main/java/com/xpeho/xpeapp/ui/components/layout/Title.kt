package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun Title(
    label: String
) {
    Text(
        text = label,
        fontSize = 18.sp,
        fontFamily = XpehoFonts.raleway,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 15.dp)
    )
}