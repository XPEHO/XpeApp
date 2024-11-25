package com.xpeho.xpeapp.ui.viewModel.newsletter.detail

import android.util.Log
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
import java.io.IOException
import java.time.ZoneId

class NewsletterDetailViewModel : ViewModel() {

    var uiState: NewsletterDetailState by mutableStateOf(
        NewsletterDetailState.LOADING
    )

    fun getNewsletterDetail(newsletterId: String) {
        viewModelScope.launch {
            uiState = try {
                val result = getNewsletterDetailFromFirebase(newsletterId)
                if (result != null) {
                    NewsletterDetailState.SUCCESS(result)
                } else {
                    NewsletterDetailState.ERROR("Error")
                }
            } catch (e: IOException) {
                NewsletterDetailState.ERROR(e.message ?: "Error")
            }
        }
    }
}

suspend fun getNewsletterDetailFromFirebase(newsletterId: String): Newsletter? {
    try {
        val db = FirebaseFirestore.getInstance()
        val result = db.collection(NEWSLETTERS_COLLECTION)
            .document(newsletterId)
            .get()
            .await()
        val defaultZone = ZoneId.systemDefault()
        val dateTimestamp = result.data?.get("date") as com.google.firebase.Timestamp
        val publicationDateTime = result.data?.get("publicationDate")
            as com.google.firebase.Timestamp
        val date = dateTimestamp.toDate().toInstant()
        val publicationDate = publicationDateTime.toDate().toInstant()
        return Newsletter(
            id = result.id,
            summary = result.data?.get("summary").toString(),
            picture = result.data?.get("previewPath").toString(),
            date = date.atZone(defaultZone).toLocalDate(),
            publicationDate = publicationDate.atZone(defaultZone).toLocalDate(),
            pdfUrl = result.data?.get("pdfUrl").toString(),
        )
    } catch (e: IOException) {
        throw e
    } catch (e: Exception) {
        Log.e("getNewsletterDetailFromFirebase", e.message ?: "Error")
        return null
    }
}
