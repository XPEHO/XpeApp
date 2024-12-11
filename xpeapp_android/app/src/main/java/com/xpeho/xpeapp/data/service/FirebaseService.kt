package com.xpeho.xpeapp.data.service

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.NEWSLETTERS_COLLECTION
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

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

    suspend fun fetchNewsletters(): List<Newsletter> {
        val newslettersList = mutableListOf<Newsletter>()
        val db = FirebaseFirestore.getInstance()
        val defaultSystemOfZone = ZoneId.systemDefault()

        try {
            db.collection(NEWSLETTERS_COLLECTION)
                .get()
                .await()
                .map {
                    val dateTimestamp = (it.data["date"] as com.google.firebase.Timestamp)
                        .toDate()
                        .toInstant()
                    val publicationDateTime =
                        (it.data["publicationDate"] as com.google.firebase.Timestamp)
                            .toDate()
                            .toInstant()
                    val newsletter = Newsletter(
                        id = it.id,
                        summary = it.data["summary"].toString(),
                        date = dateTimestamp.atZone(defaultSystemOfZone)
                            .toLocalDate(),
                        publicationDate = publicationDateTime.atZone(defaultSystemOfZone)
                            .toLocalDate(),
                        pdfUrl = it.data["pdfUrl"].toString(),
                        picture = it.data["previewPath"].toString()
                    )
                    newslettersList.add(newsletter)
                }
        } catch (firebaseException: FirebaseException) {
            Log.d("fetchNewsletters", "Error getting documents: ", firebaseException)
        }

        return newslettersList.sortedByDescending { it.date }
    }

    suspend fun getLastNewsletterPreview(previewPath: String?): ImageBitmap? {
        var imageBitmap: ImageBitmap? = null

        if (previewPath != null) {
            try {
                val storageRef = FirebaseStorage.getInstance().reference.child(previewPath)
                val bytes = storageRef.getBytes(Long.MAX_VALUE).await()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageBitmap = bitmap.asImageBitmap()
            } catch (e: FirebaseException) {
                Log.e("getLastNewsletterPreview", "Error fetching image: ", e)
            }
        }

        return imageBitmap
    }
}