package com.xpeho.xpeapp.presentation.componants.vacation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.RequestLeaveDetail
import com.xpeho.xpeapp.data.model.RequestLeaveType
import com.xpeho.xpeapp.presentation.Resources
import com.xpeho.xpeapp.ui.theme.SfPro
import java.time.format.DateTimeFormatter

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
                .fillMaxWidth(0.75f)
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MyRequestLeaveItemPreview() {
    Scaffold(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        MyRequestLeaveItem(
            requestLeave = Resources().listOfRequest.first()
        )
    }
}