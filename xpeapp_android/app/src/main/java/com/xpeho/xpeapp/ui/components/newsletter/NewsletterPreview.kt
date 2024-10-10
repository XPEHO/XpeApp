package com.xpeho.xpeapp.ui.components.newsletter

import com.xpeho.xpeho_ui_android.FilePreviewButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.Newsletter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun NewsletterPreview(newsletter: Newsletter) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val openUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    FilePreviewButton(
        labelStart = "Newsletter",
        labelEnd = formatter.format(newsletter.date),
        height = 200.dp,
        tags = {
            newsletter.summary.split(",").forEach { item ->
                TagPill(
                    label = item,
                    backgroundColor = XpehoColors.XPEHO_COLOR,
                    size = 10.sp
                )
            }
        },
        imagePreview = {
            Image(
                painter = painterResource(id = R.drawable.newsletter_preview_example),
                contentDescription = "Newsletter Preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    ){
        openNewsletter(openUrlLauncher, newsletter.pdfUrl)
    }
}

@Preview
@Composable
fun NewsletterPreviewPreview() {
    NewsletterPreview(
        newsletter = Newsletter(
            id = "1",
            summary = "Summary 1,Summary 2,Summary 3, Summary 4, Summary 5, Summary 6",
            pdfUrl = "https://www.google.com",
            date = LocalDate.now(),
            publicationDate = LocalDate.now(),
        ),
    )
}