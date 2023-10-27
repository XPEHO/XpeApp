package com.xpeho.xpeapp.presentation

import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.RequestLeave
import com.xpeho.xpeapp.data.model.RequestLeaveDetail
import com.xpeho.xpeapp.data.model.RequestLeaveStatus
import com.xpeho.xpeapp.data.model.RequestLeaveType
import java.time.LocalDate

class Resources {
    var listOfMenu: Array<Menu> =
        arrayOf(
            Menu(
                idImage = R.drawable.newsletters,
                title = "Newsletters",
                redirection = ""
            ),
            Menu(
                idImage = R.drawable.cra,
                title = "CRA",
                redirection = ""
            ),
            Menu(
                idImage = R.drawable.vacation,
                title = "Congés",
                redirection = Screens.Vacation.name,
            ),
            Menu(
                idImage = R.drawable.expense_report,
                title = "Note de frais",
                redirection = ""
            ),
            Menu(
                idImage = R.drawable.colleagues,
                title = "Mes collègues",
                redirection = ""
            ),
        )

    var listOfRequest: Array<RequestLeaveDetail> =
        arrayOf(
            RequestLeaveDetail(
                id = 1,
                startDate = LocalDate.of(2023, 10, 10),
                endDate = LocalDate.of(2023, 10, 17),
                type = RequestLeaveType.PAID,
                status = RequestLeaveStatus.ACCEPTED,
                comment = "Vacances",
            ),
            RequestLeaveDetail(
                id = 2,
                startDate = LocalDate.of(2023, 1, 2),
                endDate = LocalDate.of(2023, 1, 3),
                type = RequestLeaveType.UNPAID,
                status = RequestLeaveStatus.REFUSED,
                comment = "Je suis malade",
            ),
            RequestLeaveDetail(
                id = 3,
                startDate = LocalDate.of(2023, 2, 2),
                endDate = LocalDate.of(2023, 2, 3),
                type = RequestLeaveType.PAID,
                status = RequestLeaveStatus.PENDING,
                comment = "RDV médical",
            )
        )

    var request = RequestLeave(
        remainingDays = 25,
        details = listOfRequest,
    )
}

data class Menu(
    val idImage: Int,
    val title: String,
    val redirection: String,
)