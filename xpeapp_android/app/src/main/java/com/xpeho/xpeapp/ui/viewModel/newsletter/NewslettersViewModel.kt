package com.xpeho.xpeapp.ui.viewModel.newsletter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.Newsletter
import kotlinx.coroutines.launch

class NewsletterViewModel : ViewModel() {

    private val firebaseService = XpeApp.appModule.firebaseService

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
            state.value = firebaseService.fetchNewsletters()
            lastNewsletter.value = state.value.firstOrNull()
            lastNewsletterPreview.value = firebaseService.getLastNewsletterPreview(lastNewsletter.value?.picture)
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
}
