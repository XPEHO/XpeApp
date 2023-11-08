package com.xpeho.xpeapp.presentation.page.newsletter.detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.componants.AppLoader
import com.xpeho.xpeapp.presentation.componants.ScaffoldTemplate
import com.xpeho.xpeapp.presentation.viewModel.newsletter.detail.NewsletterDetailState
import com.xpeho.xpeapp.presentation.viewModel.newsletter.detail.NewsletterDetailViewModel

@Composable
fun NewsletterDetailPage(
    newsletterViewModel: NewsletterDetailViewModel = viewModel(),
    newsletterId: String,
    onBackPressed: () -> Unit,
) {
    newsletterViewModel.getNewsletterDetail(newsletterId)
    val uiState = newsletterViewModel.uiState
    ScaffoldTemplate(
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        onBackPressed = onBackPressed,
        appBarTitle = stringResource(id = R.string.newsletter_detail_title),
    ) {
        when (uiState) {
            is NewsletterDetailState.LOADING -> {
                AppLoader()
            }
            is NewsletterDetailState.SUCCESS -> {
                NewsletterDetailComponent(newsletter = uiState.newsletterDetail)
            }
            is NewsletterDetailState.ERROR -> {
                Text(text = uiState.message)
            }
        }
    }
}
