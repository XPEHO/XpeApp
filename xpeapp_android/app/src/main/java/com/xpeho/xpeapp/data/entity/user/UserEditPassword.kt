package com.xpeho.xpeapp.data.entity.user

data class UserEditPassword(
    val initialPassword: String,
    val password: String,
    val passwordRepeat: String,
)
