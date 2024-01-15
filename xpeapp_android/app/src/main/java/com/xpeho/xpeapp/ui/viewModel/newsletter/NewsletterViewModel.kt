package com.xpeho.xpeapp.ui.viewModel.newsletter

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.NEWSLETTERS_COLLECTION
import com.xpeho.xpeapp.data.model.Newsletter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

class NewsletterViewModel : ViewModel() {

    val state = mutableStateOf(
        listOf<Newsletter>()
    )
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        getNewsletters()
    }

    private fun getNewsletters() {
        isLoading.value = true
        viewModelScope.launch {
            state.value = getNewslettersFromFirebase()
            isLoading.value = false
        }
    }
}

suspend fun getNewslettersFromFirebase(): List<Newsletter> {
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
                )
                newslettersList.add(newsletter)
            }
    } catch (firebaseException: FirebaseException) {
        Log.d("getNewslettersFromFirebase", "Error getting documents: ", firebaseException)
    }

    return newslettersList.sortedByDescending { it.date }
}
