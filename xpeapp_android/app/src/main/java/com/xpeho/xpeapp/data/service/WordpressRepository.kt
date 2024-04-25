package com.xpeho.xpeapp.data.service

import android.util.Log
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class WordpressRepository {
    companion object {
        private const val HTTPBADREQUEST = 400
        private const val HTTPSERVICEUNAVAILABLE = 503
    }

    suspend fun authenticate(credentials: AuthentificationBody): AuthResult<WordpressToken> {
        return try {
            val token = WordpressAPI.service.authentification(credentials)
            AuthResult.Success(token)
        } catch (e: Exception) {
            handleAuthException(e)
        }
    }

    suspend fun validateToken(token: WordpressToken): AuthResult<Unit> {
        return try {
            WordpressAPI.service.validateToken("Bearer ${token.jwt_token}")
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            handleAuthException(e)
        }
    }

    suspend fun getUserId(username: String): String? {
        return try {
            WordpressAPI.service.getUserId(username)
        } catch (e: HttpException) {
            Log.e("WordpressRepository", "Unknown error: ${e.message}")
            null
        }
    }

    private fun handleAuthException(e: Exception): AuthResult<Nothing> {
        if (isNetworkError(e)){
            Log.e("WordpressRepository", "Network error: ${e.message}")
            return AuthResult.NetworkError
        }
        // Backend sends a 400 Bad Request, should send a 401 Unauthorized
        if (e is HttpException && e.code() == HTTPBADREQUEST)
            return AuthResult.Unauthorized
        Log.e("WordpressRepository", "Unknown error: ${e.message}")
        throw e
    }

    private fun isNetworkError(e: Exception): Boolean {
        return when (e) {
            is UnknownHostException -> true
            is SocketTimeoutException -> true
            is SSLHandshakeException -> true
            is ConnectException -> true
            is HttpException -> e.code() == HTTPSERVICEUNAVAILABLE
            else -> false
        }
    }
}

sealed interface AuthResult<out T> {
    object Unauthorized: AuthResult<Nothing>
    object NetworkError: AuthResult<Nothing>
    data class Success<out T>(val data: T): AuthResult<T>
}
