package com.xpeho.xpeapp.domain

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.toImmutableMap

class FeatureFlippingManager {
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
            getFeatureFlippingFromFirebase()
        } catch (e: Exception) {
            _featuresState.value = FeatureFlippingState.ERROR("Error: ${e.message}")
            return
        }
        val featureEnabled = mutableMapOf<FeatureFlippingEnum, Boolean>()
        for (feature in featureFlippingList) {
            val enumOfFeature = FeatureFlippingEnum.entries.find { it.value == feature.id }
            if (enumOfFeature == null) {
                _featuresState.value = FeatureFlippingState.ERROR(
                    "Error: A feature is not found in the FeatureFlippingEnum."
                )
                return
            }
            featureEnabled[enumOfFeature] =
                if (BuildConfig.ENVIRONMENT == "prod") feature.prodEnabled else feature.uatEnabled
        }
        _featuresState.value = FeatureFlippingState.SUCCESS(featureEnabled.toImmutableMap())
    }

    // Get the list of features from Firebase
    private suspend fun getFeatureFlippingFromFirebase(): List<FeatureFlipping> {
        try {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection(FEATURE_FLIPPING_COLLECTION)
                .get()
                .await()

            val featureFlippingList = mutableListOf<FeatureFlipping>()
            for (doc in document.documents) {
                val featureFlipping = doc.toFeatureFlipping()
                if (!featureFlippingList.contains(featureFlipping)) {
                    featureFlippingList.add(featureFlipping)
                } else {
                    featureFlippingList[featureFlippingList.indexOf(featureFlipping)] = featureFlipping
                }
            }
            return featureFlippingList
        } catch (firebaseException: FirebaseException) {
            Log.e("getFeatureFlippingFromFirebase", "Error getting documents: $firebaseException")
            return emptyList()
        }
    }


}

sealed interface FeatureFlippingState {
    object LOADING : FeatureFlippingState
    data class ERROR(val error: String) : FeatureFlippingState
    data class SUCCESS(val featureEnabled: Map<FeatureFlippingEnum, Boolean>) : FeatureFlippingState
}