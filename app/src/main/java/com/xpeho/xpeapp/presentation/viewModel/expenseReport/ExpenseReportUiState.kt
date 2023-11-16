package com.xpeho.xpeapp.presentation.viewModel.expenseReport

import com.xpeho.xpeapp.data.model.ExpenseReport

interface ExpenseReportUiState {

    object LOADING : ExpenseReportUiState
    data class ERROR(val error: String) : ExpenseReportUiState
    data class SUCCESS(val expenses: List<ExpenseReport>) : ExpenseReportUiState
    data class EMPTY(val message: String) : ExpenseReportUiState
    data class DETAIL_SUCCESS(val expense: ExpenseReport) : ExpenseReportUiState
    data class DETAIL_ERROR(val error: String) : ExpenseReportUiState
}