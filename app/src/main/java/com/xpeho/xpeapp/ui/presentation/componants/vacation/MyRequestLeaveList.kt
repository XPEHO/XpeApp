package com.xpeho.xpeapp.ui.presentation.componants.vacation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.RequestLeaveDetail
import com.xpeho.xpeapp.ui.presentation.Resources
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun MyRequestLeaveList(list: Array<RequestLeaveDetail>) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                )
        ) {
            Text(
                text = stringResource(id = R.string.vacation_my_request_title),
                fontStyle = FontStyle.Italic,
                fontFamily = SfPro,
            )
        }
        list.forEach { requestLeave ->
            Box(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                    )
            ) {
                MyRequestLeaveItem(
                    requestLeave = requestLeave,
                )
            }
        }
    }
}

@Preview
@Composable
fun MyRequestLeaveListPreview() {
    MyRequestLeaveList(
        list = Resources().listOfRequest,
    )
}
