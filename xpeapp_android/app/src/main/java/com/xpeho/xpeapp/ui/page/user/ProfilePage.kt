package com.xpeho.xpeapp.ui.page.user

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProfilePage(
    navigationController: NavController
) {
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
            ProfileEditPasswordView(
                onComplete = { isChangingPassword = false }
            )
        } else {
            ProfileUserInfosView(onClickToAccessPasswordEdition = { isChangingPassword = true }, navigationController = navigationController)
        }
    }
}


