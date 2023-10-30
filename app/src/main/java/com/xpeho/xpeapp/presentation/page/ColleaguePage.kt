package com.xpeho.xpeapp.presentation.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.presentation.componants.colleague.HappyBirthdayComponent

@Composable
fun ColleaguePage(
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.colleague_title),
            ) {
                onBackPressed()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            HappyBirthdayComponent(
                userToBirthday = "John Doe",
            )
            Spacer(
                modifier = Modifier
                    .height(100.dp)
            )
            ButtonElevated(
                text = stringResource(id = R.string.colleague_phone_book),
                backgroundColor = Color(0xFFFF9900),
                textColor = Color.Black,
                onPressed = {},
                icon = Icons.AutoMirrored.Filled.List
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonElevated(
                text = stringResource(id = R.string.colleague_birthday_list),
                backgroundColor = Color(0xFF5AA6FF),
                textColor = Color.Black,
                onPressed = {},
                icon = Icons.Outlined.Cake
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonElevated(
                text = stringResource(id = R.string.colleague_vacation),
                backgroundColor = Color(0xFF7D818C),
                textColor = Color.White,
                onPressed = {},
                icon = ImageVector.vectorResource(id = R.drawable.vacation)
            )
        }
    }
}

@Preview
@Composable
fun ColleaguePagePreview() {
    ColleaguePage(
        onBackPressed = {}
    )
}