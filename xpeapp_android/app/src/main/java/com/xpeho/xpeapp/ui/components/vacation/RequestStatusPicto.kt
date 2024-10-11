package com.xpeho.xpeapp.ui.components.vacation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.RequestLeaveStatus
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

@Composable
fun RequestStatusPicto(status: RequestLeaveStatus) {
    val picto = when (status) {
        RequestLeaveStatus.PENDING -> R.drawable.in_progress
        RequestLeaveStatus.ACCEPTED -> XpehoRes.validated
        RequestLeaveStatus.REFUSED -> R.drawable.refused
    }

    return Image(
        modifier = Modifier
            .padding(
                end = 8.dp,
            ),
        painter = painterResource(id = picto),
        contentDescription = null,
    )
}
