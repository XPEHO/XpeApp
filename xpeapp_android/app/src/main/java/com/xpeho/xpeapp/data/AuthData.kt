package com.xpeho.xpeapp.data

import com.xpeho.xpeapp.data.model.WordpressToken

data class AuthData(
    val username: String,
    val token: WordpressToken
)