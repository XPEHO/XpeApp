package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.WordpressAPI
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthState
import com.xpeho.xpeapp.domain.AuthenticationManager
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QvstCampaignViewModel (
    private val wordpressRepo: WordpressRepository,
    private val authManager: AuthenticationManager
) : ViewModel() {

    var state: QvstUiState by mutableStateOf(QvstUiState.EMPTY)

    init {
        getAllCampaign()
    }

    private fun getAllCampaign() {
        state = QvstUiState.LOADING
        viewModelScope.launch {
            state = try {
                val authState = authManager.authState.value
                if (authState is AuthState.Authenticated) {
                    val username = authState.authData.username
                    val token = authState.authData.token

                    val campaigns = wordpressRepo.getAllQvstCampaigns(token, username)
                    if (campaigns == null) {
                        QvstUiState.ERROR("No result")
                    } else if (campaigns.isEmpty()) {
                        QvstUiState.EMPTY
                    } else {
                        val result = wordpressRepo.classifyCampaigns(campaigns)
                        QvstUiState.SUCCESS(result)
                    }
                } else {
                    QvstUiState.ERROR("User is not authenticated")
                }
            } catch (e: Exception) {
                QvstUiState.ERROR("Oups, il y a eu un problème dans le chargement des campagnes")
            }
        }
    }

    fun resetState() {
        state = QvstUiState.EMPTY
    }
}