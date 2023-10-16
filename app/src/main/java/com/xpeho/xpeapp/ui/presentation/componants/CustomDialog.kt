package com.xpeho.xpeapp.ui.presentation.componants

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    closeDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = {
                closeDialog()
            }) {
                Text("OK")
            }
        }
    )
}