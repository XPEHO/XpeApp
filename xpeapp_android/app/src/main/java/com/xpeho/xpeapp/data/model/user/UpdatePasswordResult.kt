package com.xpeho.xpeapp.data.model.user

sealed interface UpdatePasswordResult{
    object IncorrectInitialPassword : UpdatePasswordResult
    object PasswordMismatch : UpdatePasswordResult
    object Success : UpdatePasswordResult
    object NetworkError : UpdatePasswordResult
}