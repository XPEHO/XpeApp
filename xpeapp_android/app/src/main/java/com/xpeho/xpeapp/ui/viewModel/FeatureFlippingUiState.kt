package com.xpeho.xpeapp.ui.viewModel

import com.xpeho.xpeapp.data.FeatureFlippingEnum

interface FeatureFlippingUiState {
    object LOADING : FeatureFlippingUiState
    data class ERROR(val error: String) : FeatureFlippingUiState
    data class SUCCESS(val featureEnabled: Map<FeatureFlippingEnum, Boolean>) : FeatureFlippingUiState
}
