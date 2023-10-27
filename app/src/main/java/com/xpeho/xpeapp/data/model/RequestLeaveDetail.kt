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
)

data class RequestLeaveDetail(
    val id: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val type: RequestLeaveType,
    val status: RequestLeaveStatus,
    val comment: String,
)
