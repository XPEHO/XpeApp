package com.xpeho.xpeapp.ui.page.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.uiState.UserInfosUiState
import com.xpeho.xpeapp.ui.viewModel.user.UserInfosViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.InputText
import com.xpeho.xpeho_ui_android.foundations.Colors
import com.xpeho.xpeho_ui_android.foundations.Fonts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileUserInfosView(
    userInfosViewModel: UserInfosViewModel = viewModel(
        factory = viewModelFactory {
            UserInfosViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    ),
    onClickToAccessPasswordEdition: () -> Unit,
    navigationController: NavController
) {
    val userInfosState = userInfosViewModel.state

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title(label = stringResource(id = R.string.profil_page_profil_label))

        when (userInfosState) {
            is UserInfosUiState.SUCCESS -> {
                val user = userInfosState.userInfos
                InputText(
                    label = stringResource(id = R.string.login_page_email),
                    defaultInput = user.email,
                    labelSize = 14.sp,
                    inputSize = 18.sp,
                    isReadOnly = true
                )
                InputText(
                    label = stringResource(id = R.string.profil_page_lastname),
                    defaultInput = user.lastname.replaceFirstChar { it.uppercase()},
                    labelSize = 14.sp,
                    inputSize = 18.sp,
                    isReadOnly = true
                )
                InputText(
                    label = stringResource(id = R.string.profil_page_firstname),
                    defaultInput = user.firstname.replaceFirstChar { it.uppercase()},
                    labelSize = 14.sp,
                    inputSize = 18.sp,
                    isReadOnly = true
                )
            }
            is UserInfosUiState.ERROR -> {
                Text(
                    text = "Error: ${userInfosState.error}",
                    fontSize = 16.sp,
                    fontFamily = Fonts.raleway,
                    color = Color.Red,
                )
            }
            else -> {
                Text(
                    text = stringResource(id = R.string.profil_page_modify_password_loading_message),
                    fontSize = 16.sp,
                    fontFamily = Fonts.raleway,
                    color = Color.Gray,
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ClickyButton(
                label = stringResource(id = R.string.profil_page_modify_password),
                onPress = {
                    userInfosViewModel.resetPasswordUpdateState()
                    onClickToAccessPasswordEdition()
                },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ClickyButton(
                label = "Déconnexion",
                size = 16.sp,
                verticalPadding = 5.dp,
                horizontalPadding = 15.dp,
                backgroundColor = Color.White,
                labelColor = Colors.CONTENT_COLOR
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    XpeApp.appModule.authenticationManager.logout()
                }
                // Return to login page and clear the backstack
                navigationController.navigate(route = Screens.Login.name) {
                    popUpTo(Screens.Home.name) { inclusive = true }
                }
            }
        }
    }
}