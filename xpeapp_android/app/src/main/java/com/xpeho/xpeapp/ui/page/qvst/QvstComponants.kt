package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xpeho.xpeapp.data.model.qvst.QvstAnswer
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.ui.theme.SfPro
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsViewModel

@Composable
fun QvstQuestionComposable(
    question: QvstQuestion,
    isAnswered: Boolean,
    onTap: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(
                color = if (isAnswered) {
                    Color.Green
                } else {
                    Color.Red
                }.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
            )
            .fillMaxWidth()
            .clickable { onTap() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuestionTextComposable(
                question = question.question
            )
            if (isAnswered && question.userAnswer != null) {
                Text(
                    text = stringResource(
                        id = com.xpeho.xpeapp.R.string.qvst_campaign_detail_answer,
                        question.userAnswer.toString(),
                    ),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun QuestionTextComposable(
    question: String,
) {
    Text(
        text = question,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontFamily = SfPro,
            fontWeight = FontWeight.W400,
            fontStyle = FontStyle.Italic,
            color = Color.Black,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .padding(16.dp),
    )
}

@Composable
fun ShowDialogToAnswerQuestion(
    question: QvstQuestion,
    onAnswerSelected: (QvstAnswer) -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                    ),
                    modifier = Modifier
                        .padding(32.dp)
                        .align(Alignment.CenterHorizontally),
                )
                question.answers.forEach { answer ->
                    AnswerButtonComposable(
                        answer = answer,
                        onAnswerSelected = { onAnswerSelected(answer) }
                    )
                }
            }
        }
    }
}

@Composable
fun AnswerButtonComposable(
    answer: QvstAnswer,
    onAnswerSelected: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
            )
            .fillMaxWidth()
            .clickable { onAnswerSelected() }
    ) {
        Text(
            text = answer.answer,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontFamily = SfPro,
                fontWeight = FontWeight.W400,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun SubmitAnswersButton(
    vm: QvstAnswersViewModel,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = 8.dp,
        )
    ) {
        /*ButtonElevated(
            text = stringResource(id = com.xpeho.xpeapp.R.string.qvst_campaign_detail_submit_answers),
            backgroundColor = colorResource(id = com.xpeho.xpeapp.R.color.colorPrimary),
            textColor = Color.Black,
            isLoading = vm.state is QvstAnswersState.LOADING,
        ) { onClick() }*/
    }
}
