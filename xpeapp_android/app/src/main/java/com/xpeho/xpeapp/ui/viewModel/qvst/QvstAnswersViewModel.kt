package com.xpeho.xpeapp.ui.viewModel.qvst

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.entity.QvstAnswerBody
import kotlinx.coroutines.launch

class QvstAnswersViewModel : ViewModel() {

    var state: QvstAnswersState by mutableStateOf(QvstAnswersState.EMPTY)
    private val _answers = mutableStateOf<Map<String, String>>(emptyMap())
    val answers: State<Map<String, String>> get() = _answers

    fun updateAnswer(questionId: String, answerId: String) {
        _answers.value += mapOf(questionId to answerId)
    }

    fun submitAnswers(campaignId: String, userId: String) {
        state = QvstAnswersState.LOADING
        viewModelScope.launch {
            // Convert map to list of QvstAnswer
            val answers = _answers.value.map { (questionId, answer) ->
                QvstAnswerBody(
                    questionId = questionId,
                    answerId = answer,
                )
            }

            // Submit answers
            val result = XpeApp.appModule.wordpressRepository
                .submitAnswers(
                    campaignId = campaignId,
                    userId = userId,
                    answers = answers,
                )

            // Update state
            if (result) {
                state = QvstAnswersState.SAVED
            } else {
                state = QvstAnswersState.ERROR("Oups, il y a eu un problème dans l'enregistrement des réponses")
            }
        }
    }
}

interface QvstAnswersState {

    object EMPTY : QvstAnswersState
    object LOADING : QvstAnswersState
    data class ERROR(val error: String) : QvstAnswersState
    object SAVED : QvstAnswersState
}