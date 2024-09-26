package com.xpeho.xpeapp.ui.components.newsletter

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.theme.SfPro
import com.xpeho.xpeho_ui_android.ClickyButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

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
                size = 10.sp
            )
        },
        tags = {
            newsletter.summary.split(",").forEach { item ->
                TagPill(
                    label = item,
                    backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                    size = 10.sp
                )
            }
        },
        button = {
            ClickyButton(
                label = "Consulter",
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White,
                size = 16.sp,
                verticalPadding = 5.dp,
                horizontalPadding = 25.dp
            ) {
                openNewsletter(openUrlLauncher, newsletter.pdfUrl)
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = XpehoRes.newsletter),
                contentDescription = "Newsletter Icon",
                tint = XpehoColors.XPEHO_COLOR
            )
        },
        size = 18.sp,
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
