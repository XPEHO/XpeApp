package com.xpeho.xpeapp.presentation.componants.expenseReport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navArgument
import com.google.firebase.Timestamp
import com.xpeho.xpeapp.data.model.ExpenseReport
import com.xpeho.xpeapp.data.model.ExpenseReportStatus
import com.xpeho.xpeapp.data.model.ExpenseReportType
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.theme.SfPro
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ExpenseReportItem(
    item: ExpenseReport,
    navigationController: NavController,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(120.dp)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp
            )
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            )
            .pointerInput(Unit) {
                navigationController.navigate(
                    route = "${Screens.ExpenseReportDetail.name}/${item.id}",
                )
            }
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            ExpenseReportTypeComponant(type = item.type)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = item.description,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = SfPro,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = "${item.amount} ${item.currency}",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = SfPro,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                )
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        ExpenseReportStatusComponant(item.status)
    }
}

@Preview
@Composable
fun ExpenseReportItemPreview() {
    ExpenseReportItem(
        item = ExpenseReport(
            id = "1",
            description = "description",
            amount = 100.0,
            currency = "EUR",
            date = LocalDate.now(),
            status = ExpenseReportStatus.APPROVED,
            userId = "1",
            proof = "proof",
            type = ExpenseReportType.ACCOMMODATION
        ),
        navigationController = NavController(LocalContext.current)
    )
}
