package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    imageVector: ImageVector?,
    onTapBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                fontFamily = SfPro,
            )
        },
        navigationIcon = {
            IconButton(onClick = onTapBack) {
                Icon(
                    imageVector = imageVector ?: Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar(
        imageVector = null,
        onTapBack = {}
    )
}