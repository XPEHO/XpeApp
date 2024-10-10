package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun QvstCampaignDetailPage(
    qvstCampaignId: String,
    navController: NavController,
    qvstCampaignQuestionsViewModel: QvstCampaignQuestionsViewModel = viewModel(),
    qvstAnswersViewModel: QvstAnswersViewModel = viewModel(),
) {
    val userId: MutableState<String> = remember { mutableStateOf("") }
    val datastorePref = DatastorePref(LocalContext.current)

    // Load data
    val campaignViewModel = viewModel<QvstCampaignsViewModel>(
        factory = viewModelFactory {
            QvstCampaignsViewModel(
                wordpressRepo = WordpressRepository(),
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )
    var campaign: QvstCampaignEntity? = null
    LaunchedEffect(Unit) {
        MainScope().launch {
            userId.value = datastorePref.userId.first()
            qvstCampaignQuestionsViewModel.getQvstCampaignQuestions(
                campaignId = qvstCampaignId,
                userId = userId.value,
            )
            campaign = campaignViewModel.getCampaignById(campaignId = qvstCampaignId)
        }
    }

    if (campaign == null || campaign!!.completed) {
        navController.navigate(Screens.Qvst.name)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (qvstCampaignQuestionsViewModel.state) {
            is QvstCampaignQuestionsState.SUCCESS -> {

                val questions = (qvstCampaignQuestionsViewModel.state as QvstCampaignQuestionsState.SUCCESS)
                    .qvstQuestions
                val currentQuestionIndex = remember { mutableStateOf(0) }

                campaign?.let {
                    Title(
                        label = it.themeName
                    )
                }
                Spacer(modifier = Modifier.padding(50.dp))
                QvstCampaignQuestionView(
                    question = questions[currentQuestionIndex.value],
                    qvstAnswersViewModel = qvstAnswersViewModel,
                )

                // Show the pagination
                Row {
                    // Previous button
                    if (currentQuestionIndex.value > 0) {
                        Icon(
                            painter = painterResource(id = XpehoRes.chevron_left),
                            contentDescription = "Previous Button",
                            tint = XpehoColors.CONTENT_COLOR,
                            modifier = Modifier
                                .clickable(onClick = {
                                    currentQuestionIndex.value -= 1
                                })
                        )
                    }
                    Text(
                        text = "Question ${currentQuestionIndex.value + 1}/${questions.size}",
                        color = XpehoColors.XPEHO_COLOR,
                    )
                    // Next button
                    if (currentQuestionIndex.value < questions.size - 1) {
                        Icon(
                            painter = painterResource(id = XpehoRes.chevron_right),
                            contentDescription = "Next Button",
                            tint = XpehoColors.CONTENT_COLOR,
                            modifier = Modifier
                                .clickable(onClick = {
                                    currentQuestionIndex.value += 1
                                })
                        )
                    }
                }
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

            if (qvstAnswersViewModel.answers.value.size == questions.size) {
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