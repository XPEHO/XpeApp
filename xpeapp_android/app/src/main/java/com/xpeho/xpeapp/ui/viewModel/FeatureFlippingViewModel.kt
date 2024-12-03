package com.xpeho.xpeapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.domain.FeatureFlippingManager
import com.xpeho.xpeapp.domain.FeatureFlippingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeatureFlippingViewModel(
    private val featureFlippingManager: FeatureFlippingManager
) : ViewModel() {

    private val _featureFlippingState: MutableStateFlow<FeatureFlippingState> =
        MutableStateFlow(FeatureFlippingState.LOADING)
    val featureFlippingState: StateFlow<FeatureFlippingState> get() = _featureFlippingState

    init {
        viewModelScope.launch {
            featureFlippingManager.getState().collect { state ->
                _featureFlippingState.value = state
            }
        }
    }

    fun isFeatureEnabled(feature: FeatureFlippingEnum): Boolean {
        return featureFlippingManager.isFeatureEnabled(feature)
    }

    fun updateState() {
        viewModelScope.launch {
            featureFlippingManager.update()
        }
    }
}