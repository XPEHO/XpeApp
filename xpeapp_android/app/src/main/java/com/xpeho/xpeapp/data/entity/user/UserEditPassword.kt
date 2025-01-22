package com.xpeho.xpeapp.data.entity.user

import com.google.gson.annotations.SerializedName

data class UserEditPassword(
    @SerializedName("initial_password") val initialPassword: String,
    @SerializedName("password") val password: String,
    @SerializedName("password_repeat") val passwordRepeat: String,


)
