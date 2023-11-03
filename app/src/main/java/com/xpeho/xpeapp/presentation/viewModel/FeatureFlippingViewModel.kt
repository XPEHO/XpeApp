package com.xpeho.xpeapp.presentation.viewModel

import android.util.Log
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.emptyFeatureFlipping
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeatureFlippingViewModel : ViewModel() {

    var uiState: FeatureFlippingUiState by mutableStateOf(FeatureFlippingUiState.LOADING)

    private val listOfFeatureFlipping = mutableListOf<FeatureFlipping>()

    val featuresState = mutableStateOf(listOfFeatureFlipping)

    fun getFeatureFlipping() {
        viewModelScope.launch {
            uiState = try {
                val result = getFeatureFlippingFromFirebase()
                featuresState.value = result.toMutableList()
                FeatureFlippingUiState.SUCCESS(result)
            } catch (firebaseException: FirebaseException) {
                FeatureFlippingUiState.ERROR(firebaseException.message ?: "")
            }
        }
    }
}

// Get the list of features from Firebase
suspend fun getFeatureFlippingFromFirebase() : List<FeatureFlipping> {
    try {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection(FEATURE_FLIPPING_COLLECTION)
            .get()
            .await()

        val featureFlippingList = mutableListOf<FeatureFlipping>()
        for (doc in document.documents) {
            val featureFlipping = doc.toFeatureFlipping()
            if(!featureFlippingList.contains(featureFlipping)) {
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

@Composable
fun FeatureFlippingComposable(
    viewModel: FeatureFlippingViewModel,
    featureId: String,
    composableAuthorized: @Composable () -> Unit,
) {
    // Get the list of features
    val featuresState = viewModel.featuresState.value

    // Get the feature from the list
    val feature = featuresState.find { it.id == featureId } ?: emptyFeatureFlipping()

    if (feature.enabled) {
        // Show the composable
        composableAuthorized()
    } else {
        // Show a empty container
        SelectionContainer {}
    }
}