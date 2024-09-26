package com.xpeho.xpeapp.ui

import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.model.RequestLeave
import com.xpeho.xpeapp.data.model.RequestLeaveDetail
import com.xpeho.xpeapp.data.model.RequestLeaveStatus
import com.xpeho.xpeapp.data.model.RequestLeaveType
import com.xpeho.xpeapp.enums.Screens
import java.time.LocalDate
import java.time.Month
import java.time.Year
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

class Resources {

    var listOfMenu: Array<Menu> =
        arrayOf(
            Menu(
                idImage = XpehoRes.newsletter,
                title = "Newsletters",
                redirection = Screens.Newsletters.name,
                featureFlippingId = FeatureFlippingEnum.NEWSLETTERS,
            ),
            Menu(
                idImage = XpehoRes.qvst,
                title = "Campagnes QVST",
                redirection = Screens.Qvst.name,
                featureFlippingId = FeatureFlippingEnum.QVST,
            ),
            Menu(
                idImage = XpehoRes.receipt,
                title = "Notes de frais",
                redirection = "",
                featureFlippingId = FeatureFlippingEnum.EXPENSE_REPORT,
            ),
            Menu(
                idImage = XpehoRes.contactfill,
                title = "Collègues",
                redirection = Screens.Colleague.name,
                featureFlippingId = FeatureFlippingEnum.COLLEAGUES,
            ),
            Menu(
                idImage = XpehoRes.briefcase,
                title = "CRA",
                redirection = "",
                featureFlippingId = FeatureFlippingEnum.CRA,
            ),
            Menu(
                idImage = XpehoRes.planedeparture,
                title = "Congés",
                redirection = Screens.Vacation.name,
                featureFlippingId = FeatureFlippingEnum.VACATION,
            ),
        )

    var listOfRequest: Array<RequestLeaveDetail> =
        arrayOf(
            RequestLeaveDetail(
                id = 1,
                startDate = LocalDate.of(Year.now().value, Month.APRIL.value, MOCK_DAY_TEN),
                endDate = LocalDate.of(Year.now().value, Month.APRIL.value, MOCK_DAY_SEVENTEEN),
                type = RequestLeaveType.PAID,
                status = RequestLeaveStatus.ACCEPTED,
                comment = "Vacances",
            ),
            RequestLeaveDetail(
                id = 2,
                startDate = LocalDate.of(Year.now().value, Month.DECEMBER.value, MOCK_DAY_ONE),
                endDate = LocalDate.of(Year.now().value, Month.DECEMBER.value, MOCK_DAY_TWO),
                type = RequestLeaveType.UNPAID,
                status = RequestLeaveStatus.REFUSED,
                comment = "Je suis malade",
            ),
            RequestLeaveDetail(
                id = 3,
                startDate = LocalDate.of(Year.now().value, Month.FEBRUARY.value, MOCK_DAY_ONE),
                endDate = LocalDate.of(Year.now().value, Month.FEBRUARY.value, MOCK_DAY_TWO),
                type = RequestLeaveType.PAID,
                status = RequestLeaveStatus.PENDING,
                comment = "RDV médical",
            )
        )

    var request = RequestLeave(
        remainingDays = 25,
        details = listOfRequest,
    )

    companion object {
        const val MOCK_DAY_ONE = 1
        const val MOCK_DAY_TWO = 2
        const val MOCK_DAY_TEN = 10
        const val MOCK_DAY_SEVENTEEN = 17
    }
}

data class Menu(
    val idImage: Int,
    val title: String,
    val redirection: String,
    val featureFlippingId: FeatureFlippingEnum,
)
