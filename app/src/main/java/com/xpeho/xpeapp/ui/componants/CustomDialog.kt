package com.xpeho.xpeapp.ui.componants

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomDialog(
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
