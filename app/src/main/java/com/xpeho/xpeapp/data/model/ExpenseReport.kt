package com.xpeho.xpeapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate

enum class ExpenseReportStatus {
    APPROVED,
    REJECTED,
    IN_PROGRESS,
}

enum class ExpenseReportType {
    KILOMETER,
    ACCOMMODATION,
    RESTAURANT,
    TRANSPORT,
    OTHER,
}

data class ExpenseReport(
    val id: String,
    val description: String,
    val amount: Double,
    val currency: String,
    @ServerTimestamp
    val date: LocalDate,
    val status: ExpenseReportStatus,
    val userId: String,
    val proof: String,
    val type: ExpenseReportType,
) {
    companion object {
        fun fromDocument(doc: DocumentSnapshot?): ExpenseReport {
            return ExpenseReport(
                id = doc?.id ?: "",
                description = doc?.getString("description") ?: "",
                amount = doc?.getDouble("amount") ?: 0.0,
                currency = doc?.getString("currency") ?: "",
                date = doc?.getTimestamp("date")?.toDate()?.toInstant()?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate() ?: LocalDate.now(),
                status = ExpenseReportStatus.valueOf(doc?.getString("status") ?: ""),
                userId = doc?.getString("userId") ?: "",
                proof = doc?.getString("proof") ?: "",
                type = ExpenseReportType.valueOf(doc?.getString("type") ?: ""),
            )
        }
    }
}
