package com.xpeho.xpeapp.ui.viewModel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.ALPHA_BOX
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.emptyFeatureFlipping
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.immutableListOf
import okhttp3.internal.toImmutableList

class FeatureFlippingViewModel : ViewModel() {

    var uiState: FeatureFlippingUiState by mutableStateOf(FeatureFlippingUiState.LOADING)

    val featuresState = mutableStateOf(immutableListOf<FeatureFlipping>())

    init {
        viewModelScope.launch {
            getFeatureFlipping()
        }
    }

    private suspend fun getFeatureFlipping() {
        uiState = try {
            val result = getFeatureFlippingFromFirebase()
            featuresState.value = result.toImmutableList()
            FeatureFlippingUiState.SUCCESS(result)
        } catch (firebaseException: FirebaseException) {
            FeatureFlippingUiState.ERROR(firebaseException.message ?: "")
        }
    }
}

// Get the list of features from Firebase
suspend fun getFeatureFlippingFromFirebase(): List<FeatureFlipping> {
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

@Composable
fun FeatureFlippingComposable(
    viewModel: FeatureFlippingViewModel,
    featureId: String,
    showIfNotEnabled: Boolean = true,
    redirection: () -> Unit,
    composableAuthorized: @Composable () -> Unit,
) {
    // Get the list of features
    val featuresState = viewModel.featuresState.value

    // Get the feature from the list
    val feature = featuresState.find { it.id == featureId } ?: emptyFeatureFlipping()

    // Get the environment from the feature
    val featureEnabled = if(BuildConfig.ENVIRONMENT == "prod") {
        feature.prodEnabled
    } else {
        feature.uatEnabled
    }

    if (featureEnabled) {
        // Show the composable
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    redirection()
                }
        ) {
            composableAuthorized()
        }
    } else {
        if(showIfNotEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(16.dp),
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                        .alpha(ALPHA_BOX)
                ) {
                    composableAuthorized()
                }
            }
        }
    }
}
