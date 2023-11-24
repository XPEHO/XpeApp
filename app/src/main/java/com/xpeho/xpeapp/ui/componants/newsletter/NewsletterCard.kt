package com.xpeho.xpeapp.ui.componants.newsletter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.theme.SfPro
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NewsletterCard(newsletter: Newsletter) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(60.dp)
            .padding(10.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            )
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 4.dp,
                ),
            imageVector = Icons.Default.Newspaper,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                ),
            text = stringResource(
                id = R.string.newsletter_card_date,
                formatter.format(newsletter.date)
            ),
            fontFamily = SfPro,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Icon(
            modifier = Modifier
                .padding(
                    end = 16.dp,
                ),
            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun NewsletterCardPreview() {
    NewsletterCard(
        newsletter = Newsletter(
            id = "1",
            summary = "Newsletter 1",
            pdfUrl = "https://www.google.com",
            date = LocalDate.now(),
            publicationDate = LocalDate.now(),
        )
    )
}
