package com.xpeho.xpeapp.ui.viewModel.newsletter

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xpeho.xpeapp.data.NEWSLETTERS_COLLECTION
import com.xpeho.xpeapp.data.model.Newsletter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

class NewsletterViewModel : ViewModel() {

    val state = mutableStateOf(
        listOf<Newsletter>()
    )
    val lastNewsletter = mutableStateOf<Newsletter?>(null)
    val lastNewsletterPreview = mutableStateOf<ImageBitmap?>(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        getNewsletters()
    }

    private fun getNewsletters() {
        isLoading.value = true
        viewModelScope.launch {
            state.value = getNewslettersFromFirebase()
            lastNewsletter.value = state.value.firstOrNull()
            lastNewsletterPreview.value = getLastNewsletterPreview()
            isLoading.value = false
        }
    }

    fun getClassifiedNewsletters(): Map<Int, List<Newsletter>> {
        return state.value.groupBy { it.date.year }
    }

    private fun resetState() {
        state.value = emptyList()
    }

    fun updateState() {
        resetState()
        getNewsletters()
    }

    private suspend fun getNewslettersFromFirebase(): List<Newsletter> {
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
            Log.d("getNewslettersFromFirebase", "Error getting documents: ", firebaseException)
        }

        return newslettersList.sortedByDescending { it.date }
    }

    private suspend fun getLastNewsletterPreview(): ImageBitmap? {
        val lastNewsletter = lastNewsletter.value
        val previewPath = lastNewsletter?.picture
        var imageBitmap: ImageBitmap? = null

        if (lastNewsletter != null && previewPath != null) {
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
