package com.xpeho.xpeapp.ui.page.user

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.entity.user.UserEditPassword
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.viewModel.user.UserInfosViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.InputText
import com.xpeho.xpeho_ui_android.foundations.Colors

@Composable
fun ProfileEditPasswordView(
    userInfosViewModel: UserInfosViewModel = viewModel(
        factory = viewModelFactory {
            UserInfosViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    ),
    onChangePassword: () -> Unit
) {
    val passwordFocusRequester = remember { FocusRequester() }
    var initialPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title(label = stringResource(id = R.string.profil_page_modify_password))

        InputText(
            label = stringResource(id = R.string.profil_page_modify_password_initial),
            labelSize = 14.sp,
            inputSize = 18.sp,
            password = true,
            onKeyboardAction = { passwordFocusRequester.requestFocus() },
            focusRequester = passwordFocusRequester,
            keyboardAction = ImeAction.Done,
            onInput = {initialPassword = it },
        )

        InputText(
            label = stringResource(id = R.string.profil_page_modify_password_new),
            labelSize = 14.sp,
            inputSize = 18.sp,
            password = true,
            focusRequester = passwordFocusRequester,
            keyboardAction = ImeAction.Done,
            onKeyboardAction = {passwordFocusRequester.requestFocus() },
            onInput = {newPassword = it }
        )

        InputText(
            label = stringResource(id = R.string.profil_page_modify_password_confirm),
            labelSize = 14.sp,
            inputSize = 18.sp,
            password = true,
            focusRequester = passwordFocusRequester,
            keyboardAction = ImeAction.Done,
            onKeyboardAction = {passwordFocusRequester.requestFocus()},
            onInput = { confirmPassword = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ClickyButton(
                label = stringResource(id = R.string.profil_page_modify_password_cancel),
                onPress = {
                    onChangePassword()
                },
                backgroundColor = Color.White,
                labelColor = Colors.CONTENT_COLOR
            )
            Spacer(modifier = Modifier.width(8.dp))

            ClickyButton(
                label = stringResource(id = R.string.profil_page_modify_password_save),
                onPress = {
//                    if (newPassword == confirmPassword) {
//                        userInfosViewModel.updatePassword(UserEditPassword(
//                            initialPassword, newPassword, confirmPassword))
//                        onChangePassword()
//                    } else {
//                        // Handle password mismatch error
//                    }
                }
            )
        }
    }
}