package com.xpeho.xpeapp.ui.components.newsletter

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun NewsletterCard(newsletter: Newsletter, open: Boolean) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val openUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    CollapsableCard(
        label = "Newsletter",
        headTag = {
            TagPill(
                label = formatter.format(newsletter.date),
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp
            )
        },
        tags = {
            newsletter.summary.split(",").forEach { item ->
                TagPill(
                    label = item,
                    backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                    size = 9.sp
                )
            }
        },
        button = {
            ClickyButton(
                label = "Consulter",
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White,
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp
            ) {
                openNewsletter(openUrlLauncher, newsletter.pdfUrl)
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = XpehoRes.newsletter),
                contentDescription = "Newsletter Icon",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier
                    .size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = open
    )
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
fun NewsletterCardPreview() {
    NewsletterCard(
        newsletter = Newsletter(
            id = "1",
            summary = "Summary 1,Summary 2,Summary 3, Summary 4, Summary 5, Summary 6",
            pdfUrl = "https://www.google.com",
            date = LocalDate.now(),
            publicationDate = LocalDate.now(),
        ),
        open = true,
    )
}
