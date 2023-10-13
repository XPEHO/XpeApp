package com.xpeho.xpeapp.presentation

import com.xpeho.xpeapp.R

class Resources {
    var listOfMenu: Array<Menu> =
        arrayOf(
            Menu(
                idImage = R.drawable.newsletters,
                title = "Newsletters"
            ),
            Menu(
                idImage = R.drawable.cra,
                title = "CRA"
            ),
            Menu(
                idImage = R.drawable.vacation,
                title = "Congés"
            ),
            Menu(
                idImage = R.drawable.expense_report,
                title = "Note de frais"
            ),
            Menu(
                idImage = R.drawable.colleagues,
                title = "Mes collègues"
            ),
        )
}

data class Menu(
    val idImage: Int,
    val title: String,
)