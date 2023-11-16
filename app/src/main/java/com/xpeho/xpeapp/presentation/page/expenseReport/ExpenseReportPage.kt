package com.xpeho.xpeapp.presentation.page.expenseReport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.ExpenseReport
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.AppLoader
import com.xpeho.xpeapp.presentation.componants.CenteredText
import com.xpeho.xpeapp.presentation.componants.expenseReport.ExpenseReportItem
import com.xpeho.xpeapp.presentation.viewModel.expenseReport.ExpenseReportUiState
import com.xpeho.xpeapp.presentation.viewModel.expenseReport.ExpenseReportViewModel

@Composable
fun ExpenseReportPage(
    userId: String,
    viewModel: ExpenseReportViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigator: NavController,
    onBackPressed: () -> Unit,
) {
    viewModel.getExpenseReport(userId)
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.expense_report_title),
            ) {
                onBackPressed()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = Color.White,
            ) {
                Icon(
                    Icons.Filled.Add,
                    null,
                )
            }
        }
    ) { paddingValues ->
        when (viewModel.uiState) {
            is ExpenseReportUiState.LOADING -> AppLoader()
            is ExpenseReportUiState.ERROR -> CenteredText(
                paddingValues = paddingValues,
                text = (viewModel.uiState as ExpenseReportUiState.ERROR).error,
            )
            is ExpenseReportUiState.EMPTY -> CenteredText(
                paddingValues = paddingValues,
                text = (viewModel.uiState as ExpenseReportUiState.EMPTY).message,
            )
            is ExpenseReportUiState.SUCCESS -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                items((viewModel.expensesState.value)) { item: ExpenseReport ->
                    ExpenseReportItem(
                        item,
                        navigator,
                    )
                }
            }
        }
    }
}
