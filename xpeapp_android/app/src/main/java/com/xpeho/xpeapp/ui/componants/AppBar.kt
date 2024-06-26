package com.xpeho.xpeapp.ui.componants

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.theme.SfPro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String?,
    imageVector: ImageVector?,
    actions:  @Composable() (RowScope.() -> Unit)? = null,
    onTapBack: () -> Unit,
) {
    var isBackTapped by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Text(
                text = title ?: stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                fontFamily = SfPro,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (!isBackTapped)
                    onTapBack()
                isBackTapped = true
            }) {
                Icon(
                    imageVector = imageVector ?: Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = (actions ?: {}),
    )
}

@Preview
@Composable
fun appBarPreview() {
    AppBar(
        imageVector = null,
        onTapBack = {},
        title = null,
    )
}
