package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.components.qvst.QvstCardList
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun QvstPage(
    navigationController: NavController,
) {
    val showClosedCampaigns = remember {
        mutableStateOf(false)
    }

    val campaignViewModel = viewModel<QvstCampaignsViewModel>(
        factory = viewModelFactory {
            QvstCampaignsViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )

    LaunchedEffect(Unit) {
        campaignViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize(),
    ) {

        when (campaignViewModel.state) {
            is QvstUiState.SUCCESS -> {
                item {
                    val openCampaigns = (campaignViewModel.state as QvstUiState.SUCCESS).qvst["open"]
                    openCampaigns?.let {
                        // Display the key (year or status)
                        Title(label = "Campagnes Ouvertes")

                        // Display the list of campaigns
                        QvstCardList(
                            navigationController = navigationController,
                            campaigns = openCampaigns,
                            collapsable = true
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                if (showClosedCampaigns.value) {
                    items((campaignViewModel.state as QvstUiState.SUCCESS).qvst.entries.toList()) { (key, campaigns) ->
                        if (key != "open") {
                            // Display the key (year or status)
                            Title(label = "Campagnes pour $key")

                            // Display the list of campaigns
                            QvstCardList(
                                navigationController = navigationController,
                                campaigns = campaigns,
                                collapsable = true
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                } else {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ClickyButton(
                                label = "Voir les campagnes fermées",
                                size = 14.sp,
                                backgroundColor = Color.White,
                                labelColor = XpehoColors.CONTENT_COLOR,
                                verticalPadding = 5.dp,
                                horizontalPadding = 15.dp,
                            ) {
                                showClosedCampaigns.value = true
                            }
                        }
                    }
                }
            }

            is QvstUiState.ERROR -> {
                item {
                    CustomDialog(
                        title = stringResource(id = R.string.login_page_error_title),
                        message = (campaignViewModel.state as QvstUiState.ERROR).error,
                    ) {
                        campaignViewModel.resetState()
                    }
                }
            }
        }
    }
}