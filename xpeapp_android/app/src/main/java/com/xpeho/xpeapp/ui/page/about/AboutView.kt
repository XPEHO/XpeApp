package com.xpeho.xpeapp.ui.page.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.R
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.foundations.Colors
import com.xpeho.xpeho_ui_android.foundations.Fonts

@Composable
fun AboutView(onDismiss: () -> Unit, context: Context) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.about_view_about_label)
                    , fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                SidebarInfoSection(context)
                Spacer(modifier = Modifier.height(8.dp))
                SidebarConfidentialityButton(context)
            }
        },
        confirmButton = {
            Box (
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                ClickyButton(
                    label = stringResource(id = R.string.about_view_ok_label),
                    size = 16.sp,
                    backgroundColor = Colors.XPEHO_COLOR,
                    labelColor = Color.White,
                    verticalPadding = 8.dp,
                    horizontalPadding = 16.dp,
                    enabled = true,
                    onPress = onDismiss
                )
            }
        }
    )
}

@Composable
fun SidebarInfoSection(context: Context) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.about_view_propriety_label),
                fontSize = 16.sp,
                fontFamily = Fonts.raleway,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "XPEHO",
                fontSize = 16.sp,
                fontFamily = Fonts.raleway,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.xpeho.com"))
                    context.startActivity(intent)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.about_view_version_label)
                    + " " + BuildConfig.VERSION_NAME,
            fontSize = 16.sp,
            fontFamily = Fonts.raleway,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}

@Composable
fun SidebarConfidentialityButton(context: Context) {
    Text(
        text = stringResource(id = R.string.about_view_confidentiality_label),
        fontSize = 14.sp,
        fontFamily = Fonts.raleway,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
        color = Color.Black,
        modifier = Modifier.clickable {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/XPEHO/XpeApp/blob/main/PRIVACY_POLICY.md")
            )
            context.startActivity(intent)
        }
    )
}