package com.xpeho.xpeapp.ui.viewModel.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.entity.user.UserEditPassword
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthState
import com.xpeho.xpeapp.domain.AuthenticationManager
// import com.xpeho.xpeapp.ui.uiState.PasswordUpdateUiState
import com.xpeho.xpeapp.ui.uiState.UserInfosUiState
import kotlinx.coroutines.launch

class UserInfosViewModel (
    private val wordpressRepo: WordpressRepository,
    private val authManager: AuthenticationManager
) : ViewModel() {

    var state: UserInfosUiState by mutableStateOf(UserInfosUiState.EMPTY)
    // var passwordUpdateState: PasswordUpdateUiState by mutableStateOf(PasswordUpdateUiState.EMPTY)

    init {
        fetchUserInfos()
    }

    private fun fetchUserInfos() {
        state = UserInfosUiState.LOADING
        viewModelScope.launch {
            val authState = authManager.authState.value
            state = if (authState is AuthState.Authenticated) {

                val result = wordpressRepo.fetchUserInfos()
                if (result == null) {
                    UserInfosUiState.ERROR("Oups, il y a eu un problème dans le chargement des informations utilisateur")
                } else {
                    UserInfosUiState.SUCCESS(result)
                }
            } else {
                UserInfosUiState.ERROR("Oups, l'utilisateur n'est pas authentifié")
            }
        }
    }

//    fun updatePassword(editPassword: UserEditPassword) {
//        passwordUpdateState = PasswordUpdateUiState.LOADING
//        viewModelScope.launch {
//            try {
//                val result = wordpressRepo.updatePassword(editPassword)
//                passwordUpdateState = if (result) {
//                    PasswordUpdateUiState.SUCCESS(true)
//                } else {
//                    PasswordUpdateUiState.ERROR("La mise à jour du mot de passe a échoué.")
//                }
//            } catch (e: Exception) {
//                passwordUpdateState = PasswordUpdateUiState.ERROR("Erreur : ${e.message}")
//            }
//        }
//    }


    fun resetState() {
        state = UserInfosUiState.EMPTY
        //passwordUpdateState = PasswordUpdateUiState.EMPTY
    }

    fun updateState() {
        resetState()
        fetchUserInfos()
    }

}