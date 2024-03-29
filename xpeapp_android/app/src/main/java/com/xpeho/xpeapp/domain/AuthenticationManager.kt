package com.xpeho.xpeapp.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.AuthData
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.AuthResult
import com.xpeho.xpeapp.data.service.WordpressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * Singleton responsible for keeping track of the authentication state,
 * logging the user in and logging the user out.
 * @param wordpressRepo: Repository for wordpress authentication
 * @param datastorePref: Wrapper for the DataStore of Preferences,
 *  for storing the authentication data
 */
class AuthenticationManager(
    val wordpressRepo: WordpressRepository,
    val datastorePref: DatastorePref
) {
    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val authState = _authState.asStateFlow()

    suspend fun initialize() {
        val authData = datastorePref.getAuthData()
        if (authData == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }
        authData.let {
            val loginRes = login(it)
            if (loginRes is LoginResult.Success) {
                _authState.value = AuthState.Authenticated(it)
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    suspend fun login(username: String, password: String): LoginResult<Unit> {
        val wordpressToken = when(val res = loginWordpress(username, password)) {
            LoginResult.NetworkError -> return LoginResult.NetworkError
            LoginResult.Unauthorized -> return LoginResult.Unauthorized
            is LoginResult.Success -> res.data
        }

        when(loginFirebase(username)) {
            LoginResult.NetworkError -> return LoginResult.NetworkError
            LoginResult.Unauthorized -> return LoginResult.Unauthorized
            is LoginResult.Success -> {}
        }

        val authData = AuthData(username, wordpressToken)
        val wordpressUid = wordpressRepo.getUserId(username)
        writeAuthentication(authData, wordpressUid)
        _authState.value = AuthState.Authenticated(authData)
        return LoginResult.Success(Unit)
    }

    private suspend fun login(authData: AuthData): LoginResult<Unit> {
        val wpLoginResult = loginWordpress(authData)
        when(wpLoginResult) {
            LoginResult.NetworkError -> return LoginResult.NetworkError
            LoginResult.Unauthorized -> return LoginResult.Unauthorized
            is LoginResult.Success -> {}
        }

        when(loginFirebase(authData.username)) {
            LoginResult.NetworkError -> return LoginResult.NetworkError
            LoginResult.Unauthorized -> return LoginResult.Unauthorized
            is LoginResult.Success -> {}
        }

        val wordpressUid = wordpressRepo.getUserId(authData.username)
        writeAuthentication(authData, wordpressUid)
        _authState.value = AuthState.Authenticated(authData)
        return LoginResult.Success(Unit)
    }

    private suspend fun writeAuthentication(authData: AuthData, wordpressUid: String?) {
        datastorePref.setAuthData(authData)
        datastorePref.setIsConnectedLeastOneTime(true)
        wordpressUid?.let{ datastorePref.setUserId(it) }
    }

    suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
        datastorePref.clearAuthData()
        _authState.value = AuthState.Unauthenticated
    }

    private suspend fun loginWordpress(username: String, password: String): LoginResult<WordpressToken> {
        val wpAuthRes = wordpressRepo.authenticate(
            AuthentificationBody(username, password)
        )
        return when(wpAuthRes) {
            AuthResult.NetworkError -> {
                _authState.value = AuthState.Unauthenticated
                Log.e("AuthenticationManager", "Network error authenticating with wordpress")
                return LoginResult.NetworkError
            }
            AuthResult.Unauthorized -> {
                _authState.value = AuthState.Unauthenticated
                Log.i("AuthenticationManager", "User Unauthorized")
                return LoginResult.Unauthorized
            }
            is AuthResult.Success -> LoginResult.Success(wpAuthRes.data)
        }
    }

    private suspend fun loginWordpress(authData: AuthData): LoginResult<Unit> {
        val wpAuthRes = wordpressRepo.validateToken(authData.token)
        return when(wpAuthRes) {
            AuthResult.NetworkError -> {
                _authState.value = AuthState.Unauthenticated
                Log.e("AuthenticationManager", "Network error validating token with wordpress")
                return LoginResult.NetworkError
            }
            AuthResult.Unauthorized -> {
                _authState.value = AuthState.Unauthenticated
                Log.i("AuthenticationManager", "User Unauthorized")
                return LoginResult.Unauthorized
            }
            is AuthResult.Success -> LoginResult.Success(Unit)
        }
    }

    private suspend fun loginFirebase(username: String): LoginResult<Unit> {
        try {
            FirebaseAuth.getInstance().signInAnonymously().await()
        } catch(e: Exception) {
            _authState.value = AuthState.Unauthenticated
            Log.e("AuthenticationManager", "Error signing in to firebase", e)
            return LoginResult.NetworkError
        }

        if (!usernameInFirestoreWordpressUsers(username)){
            _authState.value = AuthState.Unauthenticated
            Log.i("AuthenticationManager", "User not in firestore")
            return LoginResult.Unauthorized
        }

        return LoginResult.Success(Unit)
    }

    private suspend fun usernameInFirestoreWordpressUsers(username: String): Boolean {
        val firestore = FirebaseFirestore.getInstance()
        val users = firestore.collection("wordpressUsers")
            .get()
            .await()
        return users.documents
            .any { user -> user["email"] == username }
    }
}

sealed class LoginResult<out T> {
    object NetworkError : LoginResult<Nothing>()
    object Unauthorized : LoginResult<Nothing>()
    data class Success<out T>(val data: T): LoginResult<T>()
}

sealed class AuthState {
    object Loading: AuthState()
    object Unauthenticated: AuthState()
    data class Authenticated(val authData: AuthData): AuthState()
}
