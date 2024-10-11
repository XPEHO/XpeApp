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

class QvstActiveCampaignsViewModel (
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
            state = try {
                val authState = authManager.authState.value
                if (authState is AuthState.Authenticated) {
                    val username = authState.authData.username
                    val token = authState.authData.token

                    val result = wordpressRepo.getActiveQvstCampaigns(token, username)

                    if (result == null) {
                        QvstActiveUiState.ERROR("No result")
                    } else if (result.isEmpty()) {
                        QvstActiveUiState.EMPTY
                    } else {
                        QvstActiveUiState.SUCCESS(result)
                    }
                } else {
                    QvstActiveUiState.ERROR("User is not authenticated")
                }
            } catch (e: Exception) {
                QvstActiveUiState.ERROR("Oups, il y a eu un problème dans le chargement des campagnes")
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