package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.service.WordpressAPI
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import kotlinx.coroutines.launch

class QvstCampaignViewModel : ViewModel() {

    var state: QvstUiState by mutableStateOf(QvstUiState.EMPTY)

    fun getActiveCampaign() {
        state = QvstUiState.LOADING
        viewModelScope.launch {
            state = try {
                val result = WordpressAPI.service.getQvstCampaigns()

                if (result.isEmpty()) {
                    QvstUiState.EMPTY
                } else {
                    QvstUiState.SUCCESS(result)
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