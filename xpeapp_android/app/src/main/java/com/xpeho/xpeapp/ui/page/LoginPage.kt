package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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

    val focusManager = LocalFocusManager.current
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Scaffold(
        content = {
            LoginPageContentColumn(
                usernameTextField,
                passwordTextField,
                errorTextFieldUser,
                errorTextFieldPassword,
                focusManager,
                usernameFocusRequester,
                passwordFocusRequester,
                wordpressState,
                onLoginPressed,
                onUsernameChange = { usernameTextField = it },
                onPasswordChange = { passwordTextField = it },
                onErrorUserChange = { errorTextFieldUser = it },
                onErrorPasswordChange = { errorTextFieldPassword = it }
            )
        }
    )
}

@Composable
private fun LoginPageContentColumn(
    usernameTextField: String,
    passwordTextField: String,
    errorTextFieldUser: Boolean,
    errorTextFieldPassword: Boolean,
    focusManager: FocusManager,
    usernameFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    wordpressState: WordpressUiState,
    onLoginPressed: (username: String, password: String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onErrorUserChange: (Boolean) -> Unit,
    onErrorPasswordChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoginPageIcon()
        Spacer(modifier = Modifier.height(100.dp))
        LoginPageInputFields(
            usernameTextField,
            passwordTextField,
            errorTextFieldUser,
            errorTextFieldPassword,
            focusManager,
            usernameFocusRequester,
            passwordFocusRequester,
            onLoginPressed,
            onUsernameChange,
            onPasswordChange,
            onErrorUserChange,
            onErrorPasswordChange
        )
        Spacer(modifier = Modifier.height(32.dp))
        LoginPageButton(
            wordpressState,
            usernameTextField,
            passwordTextField,
            onLoginPressed,
            onErrorUserChange,
            onErrorPasswordChange
        )
    }
}

@Composable
private fun LoginPageIcon() {
    Icon(
        painter = painterResource(id = R.drawable.app_icon_cropped),
        tint = XpehoColors.XPEHO_COLOR,
        contentDescription = stringResource(id = R.string.xpeho_logo_content),
        modifier = Modifier.width(200.dp)
    )
}

@Composable
private fun LoginPageInputFields(
    usernameTextField: String,
    passwordTextField: String,
    errorTextFieldUser: Boolean,
    errorTextFieldPassword: Boolean,
    focusManager: FocusManager,
    usernameFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    onLoginPressed: (username: String, password: String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onErrorUserChange: (Boolean) -> Unit,
    onErrorPasswordChange: (Boolean) -> Unit
) {
    InputText(
        label = stringResource(id = R.string.login_page_email),
        defaultInput = usernameTextField,
        labelSize = 14.sp,
        inputSize = 18.sp,
        focusRequester = usernameFocusRequester,
        keyboardAction = ImeAction.Next,
        onKeyboardAction = {
            passwordFocusRequester.requestFocus()
        }
    ) {
        onUsernameChange(it)
    }
    CheckInputField(errorTextFieldUser, R.string.login_page_enter_email_warning)
    Spacer(modifier = Modifier.height(10.dp))
    InputText(
        label = stringResource(id = R.string.login_page_password),
        defaultInput = passwordTextField,
        labelSize = 14.sp,
        inputSize = 18.sp,
        password = true,
        focusRequester = passwordFocusRequester,
        keyboardAction = ImeAction.Done,
        onKeyboardAction = {
            focusManager.clearFocus()
            onErrorUserChange(false)
            onErrorPasswordChange(false)
            if (usernameTextField.isNotEmpty() && passwordTextField.isNotEmpty()) {
                onLoginPressed(usernameTextField, passwordTextField)
            } else {
                if (usernameTextField.isEmpty()) onErrorUserChange(true)
                if (passwordTextField.isEmpty()) onErrorPasswordChange(true)
            }
        }
    ) {
        onPasswordChange(it)
    }
    CheckInputField(errorTextFieldPassword, R.string.login_page_enter_password_warning)
}

@Composable
private fun LoginPageButton(
    wordpressState: WordpressUiState,
    usernameTextField: String,
    passwordTextField: String,
    onLoginPressed: (username: String, password: String) -> Unit,
    onErrorUserChange: (Boolean) -> Unit,
    onErrorPasswordChange: (Boolean) -> Unit
) {
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
        onErrorUserChange(false)
        onErrorPasswordChange(false)
        if (usernameTextField.isNotEmpty() && passwordTextField.isNotEmpty()) {
            onLoginPressed(usernameTextField, passwordTextField)
        } else {
            if (usernameTextField.isEmpty()) onErrorUserChange(true)
            if (passwordTextField.isEmpty()) onErrorPasswordChange(true)
        }
    }
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
