package com.xpeho.xpeapp.ui.page.qvst

import ChoiceSelector
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.data.model.qvst.QvstAnswer
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.ui.components.layout.Title
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
            qvstAnswersViewModel.updateAnswer(question.question_id, answerSelected!!.id)
            question.userAnswer = answerSelected.answer
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(text = question.question)
        }
        item (key = "ChoiceSelector Question " + question.question_id) {
            ChoiceSelector(
                choicesAvailable = question.answers.map { it.answer },
                checkIconColor = XpehoColors.XPEHO_COLOR,
                label = "ChoiceSelector Question " + question.question_id,
                defaultSelectedChoice = question.userAnswer,
            ) { answer ->
                onAnswerSelected(answer)
            }
        }
    }
}