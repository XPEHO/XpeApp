package com.xpeho.xpeapp.presentation.viewModel.newsletter.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.NEWSLETTERS_COLLECTION
import com.xpeho.xpeapp.data.model.Newsletter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

class NewsletterDetailViewModel : ViewModel() {

    var uiState: NewsletterDetailState by mutableStateOf(
        NewsletterDetailState.LOADING
    )

    fun getNewsletterDetail(newsletterId: String) {
        viewModelScope.launch {
            uiState = try {
                val result = getNewsletterDetailFromFirebase(newsletterId)
                NewsletterDetailState.SUCCESS(result)
            } catch (e: Exception) {
                NewsletterDetailState.ERROR(e.message ?: "Error")
            }
        }
    }
}

suspend fun getNewsletterDetailFromFirebase(newsletterId: String): Newsletter {
    try {
        val db = FirebaseFirestore.getInstance()
        val result = db.collection(NEWSLETTERS_COLLECTION)
            .document(newsletterId)
            .get()
            .await()
        val dateTimestamp = (result.data?.get("date") as com.google.firebase.Timestamp).toDate().toInstant()
        val publicationDateTime = (result.data?.get("publicationDate") as com.google.firebase.Timestamp).toDate().toInstant()
        return Newsletter(
            id = result.id,
            summary = result.data?.get("summary").toString(),
            date = dateTimestamp.atZone(ZoneId.systemDefault()).toLocalDate(),
            publicationDate = publicationDateTime.atZone(ZoneId.systemDefault()).toLocalDate(),
            pdfUrl = result.data?.get("pdfUrl").toString(),
        )
    } catch (e: Exception) {
        throw e
    }
}