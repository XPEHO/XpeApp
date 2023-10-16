package com.xpeho.xpeapp.data.service

data class ErrorResponse(
    val status: String,
    val error: String,
    val code: String,
    val errorDescription: String
)
