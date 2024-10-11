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

class QvstCampaignsViewModel (
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

    fun updateState() {
        resetState()
        getAllCampaign()
    }
}