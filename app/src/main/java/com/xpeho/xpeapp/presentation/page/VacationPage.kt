package com.xpeho.xpeapp.presentation.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.Resources
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.vacation.MyRequestLeaveList
import com.xpeho.xpeapp.ui.theme.SfPro

@SuppressLint("NewApi")
@Composable
fun VacationPage(
    onBackPressed: () -> Unit,
) {
    val vacationRequestLeave = Resources().request
    Scaffold(
        containerColor = colorResource(id = R.color.xpeho_background_color),
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.vacation_title_app_bar)
            ) {
                onBackPressed()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                containerColor = Color.White,
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
                .fillMaxSize(),
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
    VacationPage(
        onBackPressed = {}
    )
}