package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.model.qvst.QvstAnswer
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.ui.componants.AppBar
import com.xpeho.xpeapp.ui.componants.CustomDialog
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun QvstCampaignDetailPage(
    qvstCampaignId: String,
    navController: NavController,
    qvstCampaignQuestionsViewModel: QvstCampaignQuestionsViewModel = viewModel(),
    qvstAnswersViewModel: QvstAnswersViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    val userId: MutableState<String> = remember { mutableStateOf("") }
    val datastorePref = DatastorePref(LocalContext.current)

    LaunchedEffect(Unit) {
        MainScope().launch {
            userId.value = datastorePref.userId.first()
            qvstCampaignQuestionsViewModel.getQvstCampaignQuestions(
                campaignId = qvstCampaignId,
                userId = userId.value,
            )
        }
    }

    val showDialog = remember { mutableStateOf(false) }
    val questionInDialog: MutableState<QvstQuestion?> = remember { mutableStateOf(null) }

    fun onAnswerSelected(questionId: String, answer: QvstAnswer) {
        qvstAnswersViewModel.updateAnswer(questionId, answer.id!!)
        questionInDialog.value!!.userAnswer = answer.answer
    }

    if (showDialog.value) {
        ShowDialogToAnswerQuestion(
            question = questionInDialog.value!!,
            onAnswerSelected = { answer ->
                showDialog.value = false
                onAnswerSelected(questionInDialog.value!!.question_id, answer)
            }
        )
    }
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.qvst_campaign),
            ) {
                onBack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (qvstCampaignQuestionsViewModel.state) {
                is QvstCampaignQuestionsState.SUCCESS -> {
                    QvstCampaignDetailContent(
                        qvstCampaignQuestionsViewModel = qvstCampaignQuestionsViewModel,
                        qvstAnswersViewModel = qvstAnswersViewModel,
                        showDialog = showDialog,
                        questionInDialog = questionInDialog,
                    )
                }
                is QvstCampaignQuestionsState.ERROR -> {
                    CustomDialog(
                        title = stringResource(id = R.string.home_page_error),
                        message = (qvstCampaignQuestionsViewModel.state as QvstCampaignQuestionsState.ERROR).error,
                        closeDialog = {
                            qvstCampaignQuestionsViewModel.resetState()
                        }
                    )
                }
            }
            // If all questions are answered, show a button to submit answers
            if (qvstCampaignQuestionsViewModel.state is QvstCampaignQuestionsState.SUCCESS) {
                val questions = (qvstCampaignQuestionsViewModel.state as QvstCampaignQuestionsState.SUCCESS)
                    .qvstQuestions

                val questionsSize = (questions[false]?.size ?: 0) + (questions[true]?.size ?: 0)

                if (qvstAnswersViewModel.answers.value.size == questionsSize) {
                    SubmitAnswersButton(
                        vm = qvstAnswersViewModel,
                        onClick = {
                            qvstAnswersViewModel.submitAnswers(
                                campaignId = qvstCampaignId,
                                userId = userId.value,
                            )
                        }
                    )
                }
            }
        }
        redirectOnSuccess(
            qvstAnswersViewModel = qvstAnswersViewModel,
            navController = navController,
        )
    }
}

@Composable
fun redirectOnSuccess(
    qvstAnswersViewModel: QvstAnswersViewModel,
    navController: NavController,
) {
    val message = stringResource(id = R.string.qvst_answers_saved)
    when (qvstAnswersViewModel.state) {
        is QvstAnswersState.SAVED -> {
            CustomDialog(
                title = stringResource(id = R.string.qvst_answers_saved_title),
                message = message,
                closeDialog = {
                    qvstAnswersViewModel.state = QvstAnswersState.EMPTY
                    navController.popBackStack()
                }
            )
        }
        is QvstAnswersState.ERROR -> {
            CustomDialog(
                title = stringResource(id = R.string.home_page_error),
                message = (qvstAnswersViewModel.state as QvstAnswersState.ERROR).error,
                closeDialog = {
                    qvstAnswersViewModel.state = QvstAnswersState.EMPTY
                }
            )
        }
    }
}