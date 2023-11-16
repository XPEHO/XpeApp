package com.xpeho.xpeapp.presentation.componants.expenseReport

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.ExpenseReportType

@Composable
fun ExpenseReportTypeComponant(type: ExpenseReportType) {
    return when (type) {
        ExpenseReportType.ACCOMMODATION -> {
            Image(
                painter = painterResource(id = R.drawable.accomodation),
                contentDescription = null
            )
        }
        ExpenseReportType.KILOMETER -> {
            Image(
                painter = painterResource(id = R.drawable.kilometer),
                contentDescription = null
            )
        }
        ExpenseReportType.OTHER -> {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
            )
        }
        ExpenseReportType.RESTAURANT -> {
            Image(
                painter = painterResource(id = R.drawable.restaurant),
                contentDescription = null
            )
        }
        ExpenseReportType.TRANSPORT -> {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = null,
            )
        }
    }
}
