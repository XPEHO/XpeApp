package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.uiState.WordpressUiState
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.InputText
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

/**
 * Login page
 * @param onLoginSuccess: Callback when login is successful
 */
@Composable
fun LoginPage(onLoginSuccess: () -> Unit) {
    val wordpressViewModel = viewModel<WordpressViewModel>(
        factory = viewModelFactory {
            WordpressViewModel(XpeApp.appModule.authenticationManager)
        }
    )

    // If login is successful, notify of login success
    LaunchedEffect(wordpressViewModel.wordpressState) {
        if (wordpressViewModel.wordpressState is WordpressUiState.SUCCESS) {
            onLoginSuccess()
        }
    }

    // If login fails, show error dialog
    if (wordpressViewModel.wordpressState is WordpressUiState.ERROR) {
        CustomDialog(
            title = stringResource(id = R.string.login_page_error_title),
            message = (wordpressViewModel.wordpressState as WordpressUiState.ERROR).error,
            closeDialog = { wordpressViewModel.wordpressState = WordpressUiState.EMPTY })
    }

    LoginPageContent(
        wordpressState = wordpressViewModel.wordpressState,
        onLoginPressed = { username, password ->
            wordpressViewModel.body = AuthentificationBody(username, password)
            wordpressViewModel.onLogin()
        })
}

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
private fun LoginPageContent(
    wordpressState: WordpressUiState,
    onLoginPressed: (username: String, password: String) -> Unit,
) {
    var usernameTextField by rememberSaveable { mutableStateOf("") }
    var passwordTextField by rememberSaveable { mutableStateOf("") }
    var errorTextFieldUser by rememberSaveable { mutableStateOf(false) }
    var errorTextFieldPassword by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.app_icon_without_bg),
                        contentDescription = stringResource(id = R.string.xpeho_logo_content),
                        tint = XpehoColors.XPEHO_COLOR,
                        modifier = Modifier
                            .scale(2f)
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                InputText(
                    label = stringResource(id = R.string.login_page_email),
                    defaultInput = usernameTextField,
                    labelSize = 14.sp,
                    inputSize = 18.sp
                ) {
                    usernameTextField = it
                }
                CheckInputField(errorTextFieldUser, R.string.login_page_enter_email_warning)
                Spacer(modifier = Modifier.height(10.dp))
                InputText(
                    label = stringResource(id = R.string.login_page_password),
                    defaultInput = passwordTextField,
                    labelSize = 14.sp,
                    inputSize = 18.sp,
                    password = true
                ) {
                    passwordTextField = it
                }
                CheckInputField(errorTextFieldPassword, R.string.login_page_enter_password_warning)
                Spacer(modifier = Modifier.height(32.dp))
                ClickyButton(
                    label = "SE CONNECTER",
                    size = 18.sp,
                    verticalPadding = 10.dp,
                    horizontalPadding = 50.dp,
                    backgroundColor = XpehoColors.XPEHO_COLOR,
                    labelColor = Color.White,
                    enabled = !(wordpressState is WordpressUiState.LOADING ||
                            wordpressState is WordpressUiState.SUCCESS)
                ) {
                    errorTextFieldUser = false
                    errorTextFieldPassword = false
                    if (usernameTextField.isNotEmpty() && passwordTextField.isNotEmpty()) {
                        onLoginPressed(usernameTextField, passwordTextField)
                    } else {
                        if (usernameTextField.isEmpty()) errorTextFieldUser = true
                        if (passwordTextField.isEmpty()) errorTextFieldPassword = true
                    }
                }
            }
        }
    )
}

@Composable
private fun CheckInputField(
    errorTextFieldUser: Boolean,
    stringId: Int,
) {
    if (errorTextFieldUser) {
        ErrorTextMessage(
            message = stringResource(id = stringId)
        )
    }
}

@Composable
fun ErrorTextMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        textAlign = TextAlign.Start,
        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    )
}

@Preview
@Composable
fun ErrorTextMessagePreview() {
    ErrorTextMessage(message = "Veuillez entrer votre email")
}
