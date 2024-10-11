package com.xpeho.xpeapp.data.model

sealed interface AuthResult<out T> {
    object Unauthorized : AuthResult<Nothing>
    object NetworkError : AuthResult<Nothing>
    data class Success<out T>(val data: T) : AuthResult<T>
}