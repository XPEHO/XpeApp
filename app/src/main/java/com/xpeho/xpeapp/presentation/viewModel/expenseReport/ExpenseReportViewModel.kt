package com.xpeho.xpeapp.presentation.viewModel.expenseReport

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.EXPENSE_REPORT_COLLECTION
import com.xpeho.xpeapp.data.model.ExpenseReport
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ExpenseReportViewModel : ViewModel() {
    var uiState: ExpenseReportUiState by mutableStateOf(ExpenseReportUiState.LOADING)

    private val listOfExpenseReport = mutableListOf<ExpenseReport>()

    val expensesState = mutableStateOf(listOfExpenseReport)

    val expenseDetail = mutableStateOf<ExpenseReport?>(
        null
    )

    fun getExpenseReport(userId: String) {
        viewModelScope.launch {
            uiState = try {
                val result = getExpenseReportFromFirebase(userId)
                expensesState.value = result.toMutableList()
                if (result.isEmpty()) {
                    ExpenseReportUiState.EMPTY("Aucun note de frais n'a été enregistré")
                } else {
                    ExpenseReportUiState.SUCCESS(result)
                }
            } catch (firebaseException: FirebaseException) {
                ExpenseReportUiState.ERROR(firebaseException.message ?: "")
            }
        }
    }

    fun getExpenseReportById(expenseReportId: String) {
        viewModelScope.launch {
            uiState = try {
                val result = getExpenseReportByIdFromFirebase(expenseReportId)
                expenseDetail.value = result
                ExpenseReportUiState.DETAIL_SUCCESS(
                    result!!
                )
            } catch (firebaseException: FirebaseException) {
                ExpenseReportUiState.DETAIL_ERROR(firebaseException.message ?: "")
            }
        }
    }
}

suspend fun getExpenseReportFromFirebase(userId: String): List<ExpenseReport> {
    try {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection(EXPENSE_REPORT_COLLECTION)
            .get()
            .await()
        val expenseReportList = mutableListOf<ExpenseReport>()
        for (doc in document.documents) {
            val expenseReport = ExpenseReport.fromDocument(doc)
            if (expenseReport.userId == userId) {
                expenseReportList.add(expenseReport)
            }
        }
        return expenseReportList
    } catch (firebaseException: FirebaseException) {
        Log.e("getExpenseReportFromFirebase", "Error getting expense report", firebaseException)
        return emptyList()
    }
}

suspend fun getExpenseReportByIdFromFirebase(expenseReportId: String): ExpenseReport? {
    return try {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection(EXPENSE_REPORT_COLLECTION)
            .document(expenseReportId)
            .get()
            .await()

        ExpenseReport.fromDocument(document)
    } catch (firebaseException: FirebaseException) {
        Log.e("getExpenseReportFromFirebase", "Error getting expense report", firebaseException)
        null
    }
}
