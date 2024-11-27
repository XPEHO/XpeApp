package com.xpeho.xpeapp.ui.components.newsletter

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.openPdfFile
import com.xpeho.xpeho_ui_android.FilePreviewButton
import com.xpeho.xpeho_ui_android.TagPill
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun NewsletterPreview(newsletter: Newsletter, preview: ImageBitmap? = null) {
    val formatter = DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH)
    val newsletterMonth = newsletter.date.format(formatter).replaceFirstChar { it.uppercase() }

    val context = LocalContext.current
    val openUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    FilePreviewButton(
        labelStart = "Newsletter",
        labelEnd = newsletterMonth,
        labelSize = 16.sp,
        height = 175.dp,
        tags = {
            newsletter.summary.split(",").forEach { item ->
                TagPill(
                    label = item,
                    backgroundColor = XpehoColors.XPEHO_COLOR,
                    size = 9.sp
                )
            }
        },
        imagePreview = {
            if (preview != null) {
                Image(
                    bitmap = preview,
                    contentDescription = "Newsletter Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.newsletter_placeholder),
                    contentDescription = "Newsletter Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    ) {
        openPdfFile(
            context = context,
            openUrlLauncher = openUrlLauncher,
            pdfUrl = newsletter.pdfUrl
        )
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