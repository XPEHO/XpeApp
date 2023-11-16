package com.xpeho.xpeapp.presentation.page.expenseReport

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.ExpenseReportStatus
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.AppLoader
import com.xpeho.xpeapp.presentation.componants.CenteredText
import com.xpeho.xpeapp.presentation.componants.FirestoreImage
import com.xpeho.xpeapp.presentation.componants.expenseReport.ExpenseReportStatusComponant
import com.xpeho.xpeapp.presentation.viewModel.expenseReport.ExpenseReportUiState
import com.xpeho.xpeapp.presentation.viewModel.expenseReport.ExpenseReportViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseReportDetailPage(
    viewModel: ExpenseReportViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    expenseReportId: String,
    onBackPressed: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    viewModel.getExpenseReportById(expenseReportId)
    val expenseReport = viewModel.expenseDetail.value ?: return
    val imageUri = "expenseReport/${expenseReport.userId}${expenseReport.proof}"
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = formatter.format(expenseReport.date),
                fontStyle = FontStyle.Italic,
            ) {
                onBackPressed()
            }
        },
    ) {
        when (viewModel.uiState) {
            is ExpenseReportUiState.LOADING -> AppLoader()
            is ExpenseReportUiState.DETAIL_ERROR -> CenteredText(
                paddingValues = it,
                text = (viewModel.uiState as ExpenseReportUiState.DETAIL_ERROR).error
            )
            is ExpenseReportUiState.DETAIL_SUCCESS -> Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = stringResource(
                        id = R.string.expense_report_detail_title,
                        expenseReport.description,
                    ),
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )
                Text(
                    text = stringResource(
                        id = R.string.expense_report_detail_proof,
                    ),
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )
                FirestoreImage(
                    imageUrl = imageUri,
                )
                Text(
                    text = stringResource(
                        id = R.string.expense_report_detail_total,
                        "${expenseReport.amount} ${expenseReport.currency}",
                    ),
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                )
                Text(
                    text = stringResource(
                        id = R.string.expense_report_detail_status,
                    ),
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                )
                ExpenseReportStatusComponant(expenseReport.status)
            }
        }
    }
}

