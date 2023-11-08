package com.xpeho.xpeapp.data.model

import java.time.LocalDate

enum class RequestLeaveType {
    PAID,
    UNPAID,
    ANTICIPATE,
    EXCEPTIONAL,
}

enum class RequestLeaveStatus {
    PENDING,
    ACCEPTED,
    REFUSED,
}

data class RequestLeave(
    val remainingDays: Int,
    val details: Array<RequestLeaveDetail>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestLeave

        if (remainingDays != other.remainingDays) return false
        if (!details.contentEquals(other.details)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = remainingDays
        result = 31 * result + details.contentHashCode()
        return result
    }
}

data class RequestLeaveDetail(
    val id: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val type: RequestLeaveType,
    val status: RequestLeaveStatus,
    val comment: String,
)
