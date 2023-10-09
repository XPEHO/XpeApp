package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.presentation.animation.pressClickEffect
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun ButtonElevated(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onPressed: () -> Unit,
) {
    Button(
        onClick = {},
        colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .pressClickEffect()
            .padding(
                top = 8.dp,
            )
    ) {
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = 0.dp)
                .height(52.dp)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = SfPro,
                )
            }
        )
    }
}
