package com.xpeho.xpeapp.data.service

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.tasks.await

class FirebaseService {
    suspend fun authenticate() {
        FirebaseAuth.getInstance().signInAnonymously().await()
    }

    fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null

    fun signOut() = FirebaseAuth.getInstance().signOut()

    suspend fun fetchFeatureFlipping(): List<FeatureFlipping> {
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
            Log.e("fetchFeatureFlipping", "Error getting documents: $firebaseException")
            return emptyList()
        }
    }
}