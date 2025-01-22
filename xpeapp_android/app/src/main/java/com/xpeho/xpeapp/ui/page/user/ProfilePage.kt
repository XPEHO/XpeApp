package com.xpeho.xpeapp.ui.page.user


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.viewModel.user.UserInfosViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory

@Composable
fun ProfilePage(
    navigationController: NavController
)  {
    var isChangingPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics {
                contentDescription = if (isChangingPassword) "ChangePasswordView" else "ProfileView"
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isChangingPassword) {
            ProfileEditPasswordView( onChangePassword = { isChangingPassword = false }
            )
        } else {
            ProfileUserInfosView(onChangePassword = { isChangingPassword = true }, navigationController = navigationController)
        }
    }
}


