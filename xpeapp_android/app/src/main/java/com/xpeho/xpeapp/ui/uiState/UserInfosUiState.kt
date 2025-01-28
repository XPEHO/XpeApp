package com.xpeho.xpeapp.ui.uiState

import com.xpeho.xpeapp.data.model.user.UserInfos

interface UserInfosUiState {
    object EMPTY : UserInfosUiState
    object LOADING : UserInfosUiState
    data class ERROR(val error: String) : UserInfosUiState
    data class SUCCESS(val userInfos: UserInfos) : UserInfosUiState
}