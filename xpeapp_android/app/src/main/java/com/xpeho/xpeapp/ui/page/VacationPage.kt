package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.Resources
import com.xpeho.xpeapp.ui.components.vacation.MyRequestLeaveList
import com.xpeho.xpeapp.ui.theme.SfPro
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun VacationPage() {
    val vacationRequestLeave = Resources().request
    Scaffold(
        containerColor = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.surface
            else XpehoColors.BACKGROUND_COLOR,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary
                    else Color.White,
            ) {
                Icon(
                    Icons.Filled.Add,
                    null,
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    id = R.string.vacation_remaining_days,
                    vacationRequestLeave.remainingDays
                ),
                fontFamily = SfPro,
                fontStyle = FontStyle.Italic,
            )
            MyRequestLeaveList(
                list = vacationRequestLeave.details,
            )
        }
    }
}

@Preview
@Composable
fun VacationPagePreview() {
    VacationPage()
}
