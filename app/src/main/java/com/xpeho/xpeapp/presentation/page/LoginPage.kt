package com.xpeho.xpeapp.presentation.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.enums.InputTextFieldKeyboardType
import com.xpeho.xpeapp.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.presentation.componants.InputTextField

@Composable
fun LoginPage(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.xpeho_logo),
            contentDescription = stringResource(id = R.string.xpeho_logo_content),
            modifier = Modifier
                .padding(
                    bottom = 32.dp,
                )
        )
        InputTextField(
            keyboardType = InputTextFieldKeyboardType.EMAIL,
            label = stringResource(id = R.string.login_page_email),
        )
        Box(modifier = Modifier.height(16.dp))
        InputTextField(
            keyboardType = InputTextFieldKeyboardType.PASSWORD,
            label = stringResource(id = R.string.login_page_password),
        )
        Box(modifier = Modifier.height(16.dp))
        ButtonElevated(
            text = stringResource(id = R.string.login_page_button),
            backgroundColor = colorResource(id = R.color.colorPrimary),
            textColor = Color.Black,
        ) {
            onClick()
        }
    }
}
