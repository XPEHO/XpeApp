package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstAnswersViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignQuestionsViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.InputText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun QvstCampaignDetailPage(
    qvstCampaignId: String,
    navController: NavController,
    qvstCampaignQuestionsViewModel: QvstCampaignQuestionsViewModel = viewModel(),
    qvstAnswersViewModel: QvstAnswersViewModel = viewModel(),
) {
    val userId: MutableState<String> = remember { mutableStateOf("") }
    val datastorePref = DatastorePref(LocalContext.current)
    val openAnswerField = remember { mutableStateOf("") }

    // Load data
    val campaignViewModel = viewModel<QvstCampaignsViewModel>(
        factory = viewModelFactory {
            QvstCampaignsViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )
    var campaign: QvstCampaignEntity?

    sendAnalyticsEvent("qvst_campaign_detail_page")

    LaunchedEffect(Unit) {
        MainScope().launch {
            userId.value = datastorePref.userId.first()
            qvstCampaignQuestionsViewModel.getQvstCampaignQuestions(
                campaignId = qvstCampaignId,
                userId = userId.value,
            )
        }
    }

    when (campaignViewModel.state) {
        is QvstUiState.LOADING -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is QvstUiState.SUCCESS -> {
            campaign = campaignViewModel.getCampaignById(campaignId = qvstCampaignId)
            if (campaign == null) {
                CustomDialog(
                    title = stringResource(id = R.string.qvst_answers_saved_title),
                    message = "Campagne non trouvée",
                    closeDialog = {
                        qvstCampaignQuestionsViewModel.resetState()
                        navController.navigate(Screens.Qvst.name)
                    }
                )
            } else if (campaign.completed) {
                CustomDialog(
                    title = stringResource(id = R.string.qvst_answers_saved_title),
                    message = "Campagne déjà complétée",
                    closeDialog = {
                        qvstCampaignQuestionsViewModel.resetState()
                        navController.navigate(Screens.Qvst.name)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                when (qvstCampaignQuestionsViewModel.state) {
                    is QvstCampaignQuestionsState.SUCCESS -> {

                        val questions = (qvstCampaignQuestionsViewModel.state as QvstCampaignQuestionsState.SUCCESS)
                            .qvstQuestions
                        val answers = qvstAnswersViewModel.answers.value
                        val currentQuestionIndex = remember { mutableStateOf(0) }
                        val currentQuestion = questions.getOrNull(currentQuestionIndex.value)
                        val currentAnswer = currentQuestion?.let { answers[it.questionId] }

                        Column {
                            campaign?.let {
                                Title(
                                    label = it.themeName
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            if (currentQuestion != null) {
                                QvstCampaignQuestionView(
                                    question = currentQuestion,
                                    qvstAnswersViewModel = qvstAnswersViewModel,
                                )
                            } else {
                                Text(text = "Des remarques ?")
                                Spacer(modifier = Modifier.height(10.dp))
                                InputText(
                                    label = "Remarques ?",
                                    labelSize = 16.sp,
                                    inputSize = 16.sp,
                                    onInput = {openAnswerField.value = it},
                                )
                            }
                        }

                        // Show the pagination
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 10.dp, top = 5.dp)
                                .fillMaxWidth()
                        ) {
                            // Previous button
                            Icon(
                                painter = painterResource(id = XpehoRes.arrow_left),
                                contentDescription = "Previous Button",
                                tint = if (currentQuestionIndex.value > 0) {
                                    XpehoColors.CONTENT_COLOR
                                } else {
                                    XpehoColors.DISABLED_COLOR
                                },
                                modifier = Modifier
                                    .clickable(onClick = {
                                        if (currentQuestionIndex.value > 0) {
                                            currentQuestionIndex.value -= 1
                                        }
                                    })
                            )
                            // Adding +1 because the last question is an open question (additionalRemark)
                            Text(
                                text = "Question ${currentQuestionIndex.value + 1}/${questions.size + 1}",
                                color = XpehoColors.XPEHO_COLOR,
                                fontSize = 20.sp,
                                fontFamily = XpehoFonts.raleway,
                                fontWeight = FontWeight.SemiBold,
                            )
                            // Next button
                            Icon(
                                painter = painterResource(
                                    id = if (currentQuestionIndex.value < questions.size) {
                                        XpehoRes.arrow_right
                                    } else {
                                        XpehoRes.validated
                                    }
                                ),
                                contentDescription = "Next Button",
                                tint = if (currentQuestion == null || currentAnswer != null) {
                                    XpehoColors.CONTENT_COLOR
                                } else {
                                    XpehoColors.DISABLED_COLOR
                                },
                                modifier = Modifier
                                    .size(
                                        if (currentQuestionIndex.value < questions.size) {
                                            24.dp
                                        } else {
                                            40.dp
                                        }
                                    )
                                    .clickable(onClick = {
                                        // If the current question is answered, go to the next question
                                        if (currentQuestion != null && currentAnswer != null) {
                                            if (currentQuestionIndex.value < questions.size) {
                                                currentQuestionIndex.value += 1
                                            }
                                        }
                                        // If all questions are answered, submit answers and submit the open question
                                        else if (currentQuestion == null ) {
                                            qvstAnswersViewModel.submitAnswersAndOpenQuestionAnswer(
                                                campaignId = qvstCampaignId,
                                                userId = userId.value,
                                                openAnswer = openAnswerField.value,
                                            )
                                        }
                                    }
                                    )
                            )
                        }
                    }

                    is QvstCampaignQuestionsState.ERROR -> {
                        CustomDialog(
                            title = stringResource(id = R.string.home_page_error),
                            message = (qvstCampaignQuestionsViewModel.state as QvstCampaignQuestionsState.ERROR).error,
                            closeDialog = {
                                qvstCampaignQuestionsViewModel.resetState()
                                navController.navigate(Screens.Qvst.name)
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

        else -> {
            CustomDialog(
                title = stringResource(id = R.string.home_page_error),
                message = (campaignViewModel.state as QvstUiState.ERROR).error,
                closeDialog = {
                    campaignViewModel.resetState()
                    navController.navigate(Screens.Qvst.name)
                }
            )
        }
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