package com.xpeho.xpeapp.ui.presentation.viewModel

import com.xpeho.xpeapp.data.model.FeatureFlipping

interface FeatureFlippingUiState {

    object LOADING : FeatureFlippingUiState
    data class ERROR(val error: String) : FeatureFlippingUiState
    data class SUCCESS(val featureFlipping: List<FeatureFlipping>) : FeatureFlippingUiState
}
