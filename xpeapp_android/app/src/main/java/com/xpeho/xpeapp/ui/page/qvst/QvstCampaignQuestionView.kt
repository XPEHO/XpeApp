package com.xpeho.xpeapp.ui.page.qvst

import ChoiceSelector
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersViewModel
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun QvstCampaignQuestionView(
    question: QvstQuestion,
    qvstAnswersViewModel: QvstAnswersViewModel,
) {
    fun onAnswerSelected(answer: String) {
        val answerSelected = question.answers.find { it.answer == answer }
        answerSelected.let {
            qvstAnswersViewModel.updateAnswer(question.questionId, answerSelected!!.id)
            question.userAnswer = answerSelected.answer
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(text = question.question)
        }
        item(key = "ChoiceSelector Question " + question.questionId) {
            ChoiceSelector(
                choicesAvailable = question.answers.map { it.answer },
                checkIconColor = XpehoColors.XPEHO_COLOR,
                label = "ChoiceSelector Question " + question.questionId,
                defaultSelectedChoice = question.userAnswer,
            ) { answer ->
                onAnswerSelected(answer)
            }
        }
    }
}