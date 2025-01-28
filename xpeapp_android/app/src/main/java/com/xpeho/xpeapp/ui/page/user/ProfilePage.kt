package com.xpeho.xpeapp.ui.page.user

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.xpeho.xpeapp.R

@Composable
fun ProfilePage(
    navigationController: NavController
) {
    var isChangingPassword by remember { mutableStateOf(false) }

    val contentDescription = if (isChangingPassword) {
        stringResource(id = R.string.profil_page_modify_password_content_description_edit_password)
    } else {
        stringResource(id = R.string.profil_page_modify_password_content_description_profile)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics {
                this.contentDescription = contentDescription
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isChangingPassword) {
            ProfileEditPasswordView(
                onComplete = {
                    isChangingPassword = false
                })
        } else {
            ProfileUserInfosView(
                onClickToAccessPasswordEdition = {
                    isChangingPassword = true
                },
                navigationController = navigationController
            )
        }
    }
}