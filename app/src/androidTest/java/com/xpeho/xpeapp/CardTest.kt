package com.xpeho.xpeapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.xpeho.xpeapp.ui.presentation.componants.Card
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class CardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myComposeUnitTest() {
        composeTestRule.setContent {
            Card(
                imageResource = android.R.drawable.ic_menu_report_image,
                title = "Expense Report",
                color = Color.Gray,
            )
        }

        // Check if the card is displayed
        val cardDisplayed = composeTestRule.onNodeWithText("Expense Report")
        assertTrue(cardDisplayed.assertIsDisplayed().isDisplayed())
    }
}
