package com.xpeho.xpeapp.ui.presentation.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.xpeho.xpeapp.ui.theme.Orange80
import com.xpeho.xpeapp.ui.presentation.animation.pressClickEffect
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun ButtonElevated(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    onPressed: () -> Unit,
) {
    Button(
        onClick = onPressed,
        enabled = !isLoading,
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
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                )
            }
            if (icon != null) Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .offset(x = 0.dp, y = 0.dp)
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    AppLoader()
                } else {
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
}

@Preview
@Composable
fun ButtonElevatedPreview() {
    ButtonElevated(
        text = "Button",
        backgroundColor = Orange80,
        textColor = Color.Black,
        onPressed = {},
        icon = Icons.Outlined.Cake,
    )
}
