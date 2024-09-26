package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.components.qvst.QvstCardList
import com.xpeho.xpeapp.ui.uiState.QvstActiveUiState
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstActiveCampaignViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.FilePreviewButton
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(navigationController: NavController) {

    val campaignActiveViewModel = viewModel<QvstActiveCampaignViewModel>(
        factory = viewModelFactory {
            QvstActiveCampaignViewModel(
                wordpressRepo = WordpressRepository(),
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        item {
            Title(label = "Dernière publication")
            FilePreviewButton(
                height = 250.dp,
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
        when (campaignActiveViewModel.state) {
            is QvstActiveUiState.SUCCESS -> {
                item {
                    Title(label = "À ne pas manquer !")
                }
                items((campaignActiveViewModel.state as QvstActiveUiState.SUCCESS).qvst) { campaign ->
                    QvstCardList(
                        navigationController = navigationController,
                        campaigns = listOf(campaign),
                        collapsable = false
                    )
                }
            }
            is QvstActiveUiState.ERROR -> {
                item {
                    CustomDialog(
                        title = stringResource(id = R.string.login_page_error_title),
                        message = (campaignActiveViewModel.state as QvstActiveUiState.ERROR).error,
                    ) {
                        campaignActiveViewModel.resetState()
                    }
                }
            }
        }
    }
}


