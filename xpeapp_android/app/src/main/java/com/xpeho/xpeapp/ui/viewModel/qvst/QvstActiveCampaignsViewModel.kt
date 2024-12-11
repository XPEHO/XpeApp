package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthState
import com.xpeho.xpeapp.domain.AuthenticationManager
import com.xpeho.xpeapp.ui.uiState.QvstActiveUiState
import kotlinx.coroutines.launch

class QvstActiveCampaignsViewModel(
    private val wordpressRepo: WordpressRepository,
    private val authManager: AuthenticationManager
) : ViewModel() {

    var state: QvstActiveUiState by mutableStateOf(QvstActiveUiState.EMPTY)

    init {
        getActiveCampaign()
    }

    private fun getActiveCampaign() {
        state = QvstActiveUiState.LOADING
        viewModelScope.launch {
            val authState = authManager.authState.value
            state = if (authState is AuthState.Authenticated) {
                val username = authState.authData.username

                val result = wordpressRepo.getQvstCampaigns(username, true)

                if (result == null) {
                    QvstActiveUiState.ERROR("Oups, il y a eu un problème dans le chargement des campagnes")
                } else {
                    QvstActiveUiState.SUCCESS(result)
                }
            } else {
                QvstActiveUiState.ERROR("Oups, l'utilisateur n'est pas authentifié")
            }
        }
    }

    fun resetState() {
        state = QvstActiveUiState.EMPTY
    }

    fun updateState() {
        resetState()
        getActiveCampaign()
    }
}