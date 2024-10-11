package com.xpeho.xpeapp.ui.uiState

import com.xpeho.xpeapp.data.entity.QvstCampaignEntity

interface QvstActiveUiState {

    object EMPTY : QvstActiveUiState
    object LOADING : QvstActiveUiState
    data class ERROR(val error: String) : QvstActiveUiState
    data class SUCCESS(val qvst: List<QvstCampaignEntity>) : QvstActiveUiState

}