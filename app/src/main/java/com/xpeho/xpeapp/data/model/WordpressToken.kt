package com.xpeho.xpeapp.data.model

data class WordpressToken(
    val token_type: String,
    val iat: Int,
    val expires_in: Int,
    val jwt_token: String
)
