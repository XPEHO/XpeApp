package com.xpeho.xpeapp.data.model

import org.junit.Test
import java.time.LocalDate

class RequestLeaveDetailUnitTest {

    @Test
    fun requestLeaveTest() {

        val dateTesting = LocalDate.now()

        val request = RequestLeave(
            remainingDays = 10,
            details = arrayOf(
                RequestLeaveDetail(
                    id = 1,
                    startDate = dateTesting,
                    endDate = dateTesting.plusDays(1),
                    type = RequestLeaveType.PAID,
                    status = RequestLeaveStatus.PENDING,
                    comment = "comment"
                )
            )
        )

        assert(request.remainingDays == 10)
        assert(request.details.size == 1)
        assert(
            request.details.first().startDate.isBefore(
                request.details.first().endDate
            )
        )
    }
}