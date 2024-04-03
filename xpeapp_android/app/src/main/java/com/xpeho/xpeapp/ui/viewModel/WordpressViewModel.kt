package com.xpeho.xpeapp.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.domain.AuthenticationManager
import com.xpeho.xpeapp.domain.LoginResult
import com.xpeho.xpeapp.ui.uiState.WordpressUiState
import kotlinx.coroutines.launch

class WordpressViewModel(
    var authManager: AuthenticationManager
) : ViewModel() {

    var body: AuthentificationBody? by mutableStateOf(null)
    var wordpressState: WordpressUiState by mutableStateOf(WordpressUiState.EMPTY)

    fun onLogin() {
        viewModelScope.launch {
            body?.let {
                handleOnLogin(credentials = it)
            }
        }
    }

    private suspend fun handleOnLogin(
        credentials: AuthentificationBody
    ) {
        wordpressState = WordpressUiState.LOADING
        val loginResult = authManager.login(credentials.username, credentials.password)
        wordpressState = when(loginResult) {
            LoginResult.NetworkError -> WordpressUiState.ERROR(NETWORK_ERROR_STRING)
            LoginResult.Unauthorized -> WordpressUiState.ERROR(UNAUTHORIZED_ERROR_STRING)
            is LoginResult.Success -> WordpressUiState.SUCCESS
        }
    }

    fun logout() {
        viewModelScope.launch {
            authManager.logout()
            wordpressState = WordpressUiState.EMPTY
        }
    }

    companion object {
        private const val NETWORK_ERROR_STRING = "Erreur de réseau"
        private const val UNAUTHORIZED_ERROR_STRING = "Identifiants incorrects"
    }
}