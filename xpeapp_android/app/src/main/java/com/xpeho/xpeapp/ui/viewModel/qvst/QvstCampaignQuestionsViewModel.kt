package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.data.service.WordpressAPI
import kotlinx.coroutines.launch

class QvstCampaignQuestionsViewModel : ViewModel() {

    var state: QvstCampaignQuestionsState by mutableStateOf(QvstCampaignQuestionsState.EMPTY)

    fun getQvstCampaignQuestions(
        campaignId: String,
        userId: String,
    ) {
        viewModelScope.launch {
            state = try {
                val result = WordpressAPI.service.getQvstQuestionsByCampaignId(
                    campaignId = campaignId,
                    userId = userId,
                )

                if (result.isEmpty()) {
                    QvstCampaignQuestionsState.EMPTY
                } else {
                    QvstCampaignQuestionsState.SUCCESS(
                        qvstQuestions = result
                    )
                }
            } catch (e: Exception) {
                QvstCampaignQuestionsState.ERROR("Oups, ${e.message}")
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