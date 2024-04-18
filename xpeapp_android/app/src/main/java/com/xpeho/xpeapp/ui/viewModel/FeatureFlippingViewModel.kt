package com.xpeho.xpeapp.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.toImmutableMap

class FeatureFlippingViewModel : ViewModel() {
    var uiState: FeatureFlippingUiState by mutableStateOf(FeatureFlippingUiState.LOADING)
        private set

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    private suspend fun fetchData() {
        val featureFlippingList = try {
            getFeatureFlippingFromFirebase()
        } catch (e: Exception) {
            uiState = FeatureFlippingUiState.ERROR("Error: ${e.message}")
            return
        }
        val featureEnabled = mutableMapOf<FeatureFlippingEnum, Boolean>()
        for(feature in featureFlippingList) {
            val enumOfFeature = FeatureFlippingEnum.values().find { it.value == feature.id }
            if (enumOfFeature == null) {
                uiState = FeatureFlippingUiState.ERROR(
                    "Error: A feature is not found in the FeatureFlippingEnum.")
                return
            }
            featureEnabled[enumOfFeature] =
                if(BuildConfig.ENVIRONMENT == "prod") feature.prodEnabled else feature.uatEnabled
        }
        uiState = FeatureFlippingUiState.SUCCESS(featureEnabled.toImmutableMap())
    }
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
