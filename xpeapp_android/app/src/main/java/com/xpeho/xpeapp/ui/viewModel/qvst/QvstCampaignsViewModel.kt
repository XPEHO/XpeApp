package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthState
import com.xpeho.xpeapp.domain.AuthenticationManager
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import kotlinx.coroutines.launch

class QvstCampaignsViewModel(
    private val wordpressRepo: WordpressRepository,
    private val authManager: AuthenticationManager
) : ViewModel() {

    var state: QvstUiState by mutableStateOf(QvstUiState.EMPTY)

    init {
        getAllCampaign()
    }

    fun getCampaignById(campaignId: String): QvstCampaignEntity? {
        val campaigns = (state as QvstUiState.SUCCESS).qvst
        val campaign = campaigns.values.flatten().find { it.id == campaignId }
        return campaign
    }

    private fun getAllCampaign() {
        state = QvstUiState.LOADING
        viewModelScope.launch {
            val authState = authManager.authState.value
            state = if (authState is AuthState.Authenticated) {
                val username = authState.authData.username
                val campaigns = wordpressRepo.getQvstCampaigns(username)
                if (campaigns == null) {
                    QvstUiState.ERROR("Oups, il y a eu un problème dans le chargement des campagnes")
                } else {
                    val result = wordpressRepo.classifyCampaigns(campaigns)
                    QvstUiState.SUCCESS(result)
                }
            } else {
                QvstUiState.ERROR("Oups, l'utilisateur n'est pas authentifié")
            }
        }
    }

    fun resetState() {
        state = QvstUiState.EMPTY
    }

    fun updateState() {
        resetState()
        getAllCampaign()
    }
}