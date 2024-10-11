package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.components.newsletter.NewsletterPreview
import com.xpeho.xpeapp.ui.components.qvst.QvstCardList
import com.xpeho.xpeapp.ui.uiState.QvstActiveUiState
import com.xpeho.xpeapp.ui.viewModel.newsletter.NewsletterViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstActiveCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(navigationController: NavController) {

    val campaignActiveViewModel = viewModel<QvstActiveCampaignsViewModel>(
        factory = viewModelFactory {
            QvstActiveCampaignsViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )

    val newsletterViewModel = viewModel<NewsletterViewModel>(
        factory = viewModelFactory {
            NewsletterViewModel()
        }
    )

    LaunchedEffect(Unit) {
        campaignActiveViewModel.updateState()
        newsletterViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        // If the newsletters are loaded, and there is at least one newsletter
        if (!newsletterViewModel.isLoading.value && newsletterViewModel.state.value.isNotEmpty()) {
            item {
                // Get the last newsletter as the first element of the list sorted descending by date
                val lastNewsletter = newsletterViewModel.state.value.first()
                Title(label = "Dernière publication")
                NewsletterPreview(newsletter = lastNewsletter)
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
        // When we have loaded the qvst campaigns
        when (campaignActiveViewModel.state) {
            // If we successfully loaded the campaigns
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
            // If there was an error loading the campaigns
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


