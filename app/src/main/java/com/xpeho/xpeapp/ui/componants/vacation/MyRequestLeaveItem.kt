package com.xpeho.xpeapp.ui.presentation.componants.vacation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.RequestLeaveDetail
import com.xpeho.xpeapp.data.model.RequestLeaveType
import java.time.format.DateTimeFormatter

const val FACTION_BOX_OF_DATE = 0.75f

@Composable
fun MyRequestLeaveItem(requestLeave: RequestLeaveDetail) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(50.dp))
            .background(color = Color.White)
            .padding(
                top = 12.dp,
                bottom = 12.dp,
            )
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = getTitleWithType(requestLeave.type),
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(
                    fraction = FACTION_BOX_OF_DATE,
                )
        ) {
            Text(
                text = "Du ${dateFormatter.format(requestLeave.startDate)} au ${
                    dateFormatter.format(
                        requestLeave.endDate
                    )
                }",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                )
            )
        }
        RequestStatusPicto(requestLeave.status)
    }
}

private fun getTitleWithType(type: RequestLeaveType): String {
    return when (type) {
        RequestLeaveType.PAID -> "CP/RTT"
        RequestLeaveType.UNPAID -> "Sans solde"
        RequestLeaveType.ANTICIPATE -> "Anticipé"
        RequestLeaveType.EXCEPTIONAL -> "Exceptionnel"
    }
}
