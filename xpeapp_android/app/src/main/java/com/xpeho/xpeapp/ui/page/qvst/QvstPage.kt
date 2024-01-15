package com.xpeho.xpeapp.ui.page.qvst

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.componants.qvst.QvstCampaignList
import com.xpeho.xpeapp.ui.componants.AppBar
import com.xpeho.xpeapp.ui.componants.CustomDialog
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignViewModel

@Composable
fun QvstPage(
    campaignViewModel: QvstCampaignViewModel = viewModel(),
    navigationController: NavController,
    onBackPressed: () -> Unit,
) {
    campaignViewModel.getActiveCampaign()
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.qvst_title),
            ) {
                onBackPressed()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.qvst_description),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                ),
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.CenterHorizontally),
            )
            when (campaignViewModel.state) {
                is QvstUiState.SUCCESS -> QvstCampaignList(
                    campaigns = (campaignViewModel.state as QvstUiState.SUCCESS).qvst
                ) {
                    navigationController.navigate(route = "QVST/${it.id}")
                }
                is QvstUiState.ERROR -> {
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