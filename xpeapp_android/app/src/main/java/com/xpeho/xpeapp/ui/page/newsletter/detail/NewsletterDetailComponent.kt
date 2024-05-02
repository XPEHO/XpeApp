package com.xpeho.xpeapp.ui.page.newsletter.detail

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.componants.ButtonElevated
import com.xpeho.xpeapp.ui.theme.SfPro
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NewsletterDetailComponent(newsletter: Newsletter) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val openUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
            )
            Box(modifier = Modifier.width(16.dp))
            Text(
                text = dateFormatter.format(newsletter.date),
                fontFamily = SfPro,
                fontStyle = FontStyle.Italic,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = stringResource(id = R.string.newsletter_detail_summary_title),
            style = androidx.compose.ui.text.TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontFamily = SfPro,
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            text = newsletter.summary.replace(
                oldValue = ",",
                newValue = "\n",
            ),
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontFamily = SfPro,
                fontStyle = FontStyle.Italic,
            )
        )
        Box(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                )
        ) {
            ButtonElevated(
                text = stringResource(id = R.string.newsletter_detail_open_newsletter),
                backgroundColor = colorResource(id = R.color.colorPrimary),
                textColor = Color.Black,
            ) {
                openNewsletter(openUrlLauncher, newsletter.pdfUrl)
            }
        }
    }
}

fun openNewsletter(
    openUrlLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    pdfUrl: String
) {
    val uri = Uri.parse(pdfUrl)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    openUrlLauncher.launch(intent)
}

@Preview
@Composable
fun NewsletterDetailComponentPreview() {
    val newsletter = Newsletter(
        id = "1",
        summary = "Summary",
        date = LocalDate.now(),
        publicationDate = LocalDate.now(),
        pdfUrl = "https://www.google.com",
    )
    NewsletterDetailComponent(newsletter = newsletter)
}
