package com.xpeho.xpeapp.presentation.page

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.componants.ButtonElevated
import com.xpeho.xpeapp.presentation.componants.InputTextField
import com.xpeho.xpeapp.presentation.componants.InputTextFieldKeyboardType

@Composable
fun LoginPage(
    onClick: () -> Unit,
) {

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
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
                InputTextField(
                    keyboardType = InputTextFieldKeyboardType.PASSWORD,
                    label = stringResource(id = R.string.login_page_password),
                )
                ButtonElevated(
                    text = stringResource(id = R.string.login_page_button),
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    textColor = Color.Black,
                ) {
                    onClick()
                }
            }
        }
    )
}