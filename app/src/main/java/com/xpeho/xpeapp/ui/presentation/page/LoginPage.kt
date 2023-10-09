package com.xpeho.xpeapp.ui.presentation.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.enums.InputTextFieldKeyboardType
import com.xpeho.xpeapp.ui.presentation.componants.*
import com.xpeho.xpeapp.ui.uiState.WordpressUiState
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun LoginPage(
    vm: WordpressViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
) {
    var usernameTextField by remember { mutableStateOf("") }
    var passwordTextField by remember { mutableStateOf("") }
    var errorTextFieldUser by remember { mutableStateOf(false) }
    var errorTextFieldPassword by remember { mutableStateOf(false) }

    val datastorePref = DatastorePref(LocalContext.current)

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
                    value = usernameTextField,
                    onValueChange = { usernameTextField = it },
                )
                if (errorTextFieldUser) {
                    ErrorTextMessage(message = "Veuillez entrer votre email")
                }
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    keyboardType = InputTextFieldKeyboardType.PASSWORD,
                    label = stringResource(id = R.string.login_page_password),
                    value = passwordTextField,
                    onValueChange = { passwordTextField = it },
                )
                if (errorTextFieldPassword) {
                    ErrorTextMessage(message = "Veuillez entrer votre mot de passe")
                }
                Spacer(modifier = Modifier.height(16.dp))
                ButtonElevated(
                    text = stringResource(id = R.string.login_page_button),
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    textColor = Color.Black,
                ) {
                    errorTextFieldUser = false
                    errorTextFieldPassword = false
                    if (usernameTextField.isNotEmpty() && passwordTextField.isNotEmpty()) {
                        vm.body = AuthentificationBody(
                            username = usernameTextField,
                            password = passwordTextField,
                        )

                        vm.onLogin(datastorePref)
                    } else {
                        if (usernameTextField.isEmpty()) {
                            errorTextFieldUser = true
                        }
                        if (passwordTextField.isEmpty()) {
                            errorTextFieldPassword = true
                        }
                    }
                }
                when (vm.wordpressState) {
                    // A comprendre pourquoi le apploader ne s'affiche pas
                    is WordpressUiState.LOADING -> AlertDialog(
                        onDismissRequest = { /*TODO*/ }
                    ) {
                        AppLoader()
                    }
                    is WordpressUiState.SUCCESS -> onLoginSuccess()
                    is WordpressUiState.ERROR -> {
                        ErrorDialog(
                            title = stringResource(id = R.string.login_page_error_title),
                            message = (vm.wordpressState as WordpressUiState.ERROR).error,
                        ) {
                            vm.wordpressState = WordpressUiState.EMPTY
                        }
                    }
                }
            }
        }
    )
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
