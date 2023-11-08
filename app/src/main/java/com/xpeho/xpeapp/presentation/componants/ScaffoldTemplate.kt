package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ScaffoldTemplate(
    icon: ImageVector?,
    // If onBackPressed is null, the back button will not be displayed
    onBackPressed: () -> Unit?,
    appBarTitle: String,
    child: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                imageVector = onBackPressed.let {
                    icon ?: Icons.AutoMirrored.Filled.Logout
                },
                title = appBarTitle,
            ) {
                onBackPressed()
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(
                it,
            )
        ) {
            child()
        }
    }
}
