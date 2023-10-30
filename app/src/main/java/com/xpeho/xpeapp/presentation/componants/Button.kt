package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.presentation.animation.pressClickEffect
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun ButtonElevated(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: ImageVector? = null,
    onPressed: () -> Unit,
) {
    Button(
        onClick = onPressed,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .pressClickEffect()
            .padding(
                top = 8.dp,
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            if (icon != null) Icon(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
            )
            if (icon != null) Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .offset(x = 0.dp, y = 0.dp)
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = SfPro,
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonElevatedPreview() {
    ButtonElevated(
        text = "Annuaire",
        backgroundColor = Color(0xFFFF9900),
        textColor = Color.Black,
        onPressed = {},
        icon = Icons.Outlined.Cake,
    )
}
