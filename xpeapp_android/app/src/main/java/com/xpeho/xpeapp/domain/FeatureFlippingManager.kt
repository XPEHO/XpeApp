package com.xpeho.xpeapp.domain

import androidx.annotation.VisibleForTesting
import com.google.firebase.FirebaseException
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.service.FirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableMap
import java.io.IOException

class FeatureFlippingManager(
    val firebaseService: FirebaseService
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _featuresState: MutableStateFlow<FeatureFlippingState> = MutableStateFlow(FeatureFlippingState.LOADING)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchData()
        }
    }

    fun getState() = _featuresState

    fun isFeatureEnabled(feature: FeatureFlippingEnum): Boolean {
        val currentState = _featuresState.value
        return if (currentState is FeatureFlippingState.SUCCESS) {
            currentState.featureEnabled[feature] ?: false
        } else {
            false
        }
    }

    fun update() {
        CoroutineScope(Dispatchers.IO).launch {
            fetchData()
        }
    }

    private suspend fun fetchData() {
        val featureFlippingList = try {
            firebaseService.fetchFeatureFlipping()
        } catch (e: IOException) {
            _featuresState.value = FeatureFlippingState.ERROR("Network error: ${e.message}")
            return
        } catch (e: FirebaseException) {
            _featuresState.value = FeatureFlippingState.ERROR("Firebase error: ${e.message}")
            return
        }

        val featureEnabled = mutableMapOf<FeatureFlippingEnum, Boolean>()
        var errorMessage: String? = null

        for (feature in featureFlippingList) {
            val enumOfFeature = FeatureFlippingEnum.entries.find { it.value == feature.id }
            if (enumOfFeature == null) {
                errorMessage = "Error: A feature is not found in the FeatureFlippingEnum."
                break
            }
            featureEnabled[enumOfFeature] =
                if (BuildConfig.ENVIRONMENT == "prod") feature.prodEnabled else feature.uatEnabled
        }

        _featuresState.value = if (errorMessage != null) {
            FeatureFlippingState.ERROR(errorMessage)
        } else {
            FeatureFlippingState.SUCCESS(featureEnabled.toImmutableMap())
        }
    }
}

sealed interface FeatureFlippingState {
    object LOADING : FeatureFlippingState
    data class ERROR(val error: String) : FeatureFlippingState
    data class SUCCESS(val featureEnabled: Map<FeatureFlippingEnum, Boolean>) : FeatureFlippingState
}