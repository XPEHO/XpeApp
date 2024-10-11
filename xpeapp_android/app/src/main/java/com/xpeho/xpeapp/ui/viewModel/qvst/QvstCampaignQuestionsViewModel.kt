package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion

import kotlinx.coroutines.launch

class QvstCampaignQuestionsViewModel : ViewModel() {

    var state: QvstCampaignQuestionsState by mutableStateOf(QvstCampaignQuestionsState.EMPTY)

    fun getQvstCampaignQuestions(
        campaignId: String,
        userId: String,
    ) {
        viewModelScope.launch {
            // Get questions
            val result = XpeApp.appModule.wordpressRepository.getQvstQuestionsByCampaignId(
                campaignId = campaignId,
                userId = userId,
            )

            // Update state
            if (result != null) {
                if (result.isEmpty()) {
                    state = QvstCampaignQuestionsState.EMPTY
                } else {
                    state = QvstCampaignQuestionsState.SUCCESS(
                        qvstQuestions = result
                    )
                }
            } else {
                state = QvstCampaignQuestionsState.ERROR("Erreur de chargement des questions")
            }
        }
    }

    fun resetState() {
        state = QvstCampaignQuestionsState.EMPTY
    }
}

interface QvstCampaignQuestionsState {
    object EMPTY : QvstCampaignQuestionsState
    object LOADING : QvstCampaignQuestionsState
    data class ERROR(val error: String) : QvstCampaignQuestionsState
    data class SUCCESS(
        val qvstQuestions: List<QvstQuestion>,
    ) : QvstCampaignQuestionsState
}