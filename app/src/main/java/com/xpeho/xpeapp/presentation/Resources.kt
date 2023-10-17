package com.xpeho.xpeapp.presentation

import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.componants.CardMode

class Resources {
    var listOfMenu: Array<Menu> =
        arrayOf(
            Menu(
                mode = CardMode.TITLE_AND_SUBTITLE,
                idImage = R.drawable.event,
                title = "Prochain événement",
                subTitle = "XPEHO",
            ),
            Menu(
                mode = CardMode.ICON_AND_TITLE,
                idImage = R.drawable.newsletters,
                title = "Newsletters"
            ),
            Menu(
                mode = CardMode.ICON_AND_TITLE,
                idImage = R.drawable.cra,
                title = "CRA"
            ),
            Menu(
                mode = CardMode.ICON_AND_TITLE,
                idImage = R.drawable.vacation,
                title = "Congés"
            ),
            Menu(
                mode = CardMode.ICON_AND_TITLE,
                idImage = R.drawable.expense_report,
                title = "Note de frais"
            ),
            Menu(
                mode = CardMode.ICON_AND_TITLE,
                idImage = R.drawable.colleagues,
                title = "Mes collègues"
            ),
        )
}

data class Menu(
    val mode: CardMode,
    val idImage: Int,
    val title: String,
    val subTitle: String? = null,
)