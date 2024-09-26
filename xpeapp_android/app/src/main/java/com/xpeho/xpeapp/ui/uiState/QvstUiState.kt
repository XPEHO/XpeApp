package com.xpeho.xpeapp.ui.uiState

import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign

interface QvstUiState {

    object EMPTY : QvstUiState
    object LOADING : QvstUiState
    data class ERROR(val error: String) : QvstUiState
    data class SUCCESS(val qvst: Map<String, List<QvstCampaignEntity>>) : QvstUiState

}