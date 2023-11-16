package com.xpeho.xpeapp.presentation.componants.expenseReport

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.ExpenseReportStatus

@Composable
fun ExpenseReportStatusComponant(status: ExpenseReportStatus) {
    val picto = when (status) {
        ExpenseReportStatus.IN_PROGRESS -> R.drawable.in_progress
        ExpenseReportStatus.APPROVED -> R.drawable.validated
        ExpenseReportStatus.REJECTED -> R.drawable.refused
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
