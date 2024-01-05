package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.presentation.componants.AppBar

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun QvstPage(
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.qvst_title),
            ) {
                onBackPressed()
            }
        }
    ) {
    }
}
