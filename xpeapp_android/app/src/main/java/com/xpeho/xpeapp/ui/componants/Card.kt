package com.xpeho.xpeapp.ui.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun Card(
    modifier: Modifier = Modifier,
    imageResource: Int,
    title: String,
    color: Color?,
) {
    Box(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(16.dp),
            )
            .background(
                if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceVariant else Color.White
            )
            .shadow(
                elevation = 5.dp,
                spotColor = Color.Transparent,
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                colorFilter = color?.let {
                    ColorFilter.tint(it)
                },
            )
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
                fontFamily = SfPro,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                    )
            )
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    Card(
        imageResource = R.drawable.expense_report,
        title = "Newsletters",
        color = colorResource(id = R.color.xpeho_color),
    )
}
