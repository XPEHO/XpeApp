package com.xpeho.xpeapp.ui.uiState

interface PasswordUpdateUiState {
    object EMPTY : PasswordUpdateUiState
    object LOADING : PasswordUpdateUiState
    object SUCCESS : PasswordUpdateUiState
    data class ERROR(val errorMessage: String) : PasswordUpdateUiState
}

