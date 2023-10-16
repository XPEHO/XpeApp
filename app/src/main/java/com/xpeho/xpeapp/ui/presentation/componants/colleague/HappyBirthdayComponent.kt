package com.xpeho.xpeapp.ui.presentation.componants.colleague

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun HappyBirthdayComponent(
    userToBirthday: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.85f)
            .height(100.dp)
            .padding(10.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.White,
                spotColor = Color.Gray,
                shape = RoundedCornerShape(size = 30.dp)
            )
    ) {
        // Icon cake
        Icon(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            imageVector = Icons.Outlined.Cake,
            contentDescription = null,
        )
        // Text
        Text(
            text = stringResource(
                id = R.string.happy_birthday_to,
                userToBirthday,
            ),
            modifier = Modifier
                .padding(start = 5.dp)
                .width(200.dp),
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = SfPro,
                fontStyle = FontStyle.Italic,
            )
        )
    }
}

