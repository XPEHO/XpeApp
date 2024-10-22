package com.xpeho.xpeapp.domain

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

/**
 * Singleton responsible for keeping track of the authentication state,
 * logging the user in and logging the user out.
 * @param wordpressRepo: Repository for wordpress authentication
 * @param datastorePref: Wrapper for the DataStore of Preferences,
 *  for storing the authentication data
 */
class AuthenticationManager(
    val wordpressRepo: WordpressRepository,
    val datastorePref: DatastorePref,
    val firebaseService: FirebaseService
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Unauthenticated)

    val authState = _authState.asStateFlow()

    fun restoreAuthStateFromStorage() = runBlocking {
        datastorePref.getAuthData()?.let {
            _authState.value = AuthState.Authenticated(it)
        }
    }

    suspend fun isAuthValid(): Boolean {
        return when (val authState = this.authState.value) {
            is AuthState.Unauthenticated -> false
            is AuthState.Authenticated -> {
                // Note(loucas): Order of operations here is important,
                // lazy `&&` evalutation makes this faster
                firebaseService.isAuthenticated()
                        && wordpressRepo.validateToken(authState.authData.token) is AuthResult.Success
            }
        }
    }

    suspend fun login(username: String, password: String): AuthResult<WordpressToken> = coroutineScope {
        val wpDefRes = async {
            wordpressRepo.authenticate(AuthentificationBody(username, password))
        }
        val fbDefRes = async {
            wordpressRepo.handleServiceExceptions(
                tryBody = {
                    firebaseService.authenticate()
                    return@async AuthResult.Success(Unit)
                },
                catchBody = { e ->
                    Log.e("AuthenticationManager: login", "Network error: ${e.message}")
                    return@async AuthResult.NetworkError
                }
            )
        }

        val wpRes = wpDefRes.await()
        val fbRes = fbDefRes.await()
        if (wpRes is AuthResult.NetworkError || fbRes is AuthResult.NetworkError) {
            return@coroutineScope AuthResult.NetworkError
        }
        if (wpRes is AuthResult.Unauthorized || fbRes is AuthResult.Unauthorized) {
            return@coroutineScope AuthResult.Unauthorized
        }

        val authData = AuthData(username, (wpRes as AuthResult.Success).data)
        writeAuthentication(authData)
        _authState.value = AuthState.Authenticated(authData)
        return@coroutineScope wpRes
    }

    private suspend fun writeAuthentication(authData: AuthData) {
        val wordpressUid = wordpressRepo.getUserId(authData.username)
        datastorePref.setAuthData(authData)
        datastorePref.setIsConnectedLeastOneTime(true)
        datastorePref.setWasConnectedLastTime(true)
        wordpressUid?.let { datastorePref.setUserId(it) }
    }

    suspend fun logout() {
        firebaseService.signOut()
        datastorePref.clearAuthData()
        datastorePref.setWasConnectedLastTime(false)
        _authState.value = AuthState.Unauthenticated
    }
}

sealed interface AuthState {
    object Unauthenticated : AuthState
    data class Authenticated(val authData: AuthData) : AuthState
}

data class AuthData(val username: String, val token: WordpressToken)