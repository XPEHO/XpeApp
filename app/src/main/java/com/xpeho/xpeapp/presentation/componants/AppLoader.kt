package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun AppLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .testTag("circularProgressIndicator")
        )
    }
}