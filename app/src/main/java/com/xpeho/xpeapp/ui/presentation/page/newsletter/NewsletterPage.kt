package com.xpeho.xpeapp.ui.presentation.page.newsletter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.presentation.componants.AppLoader
import com.xpeho.xpeapp.ui.presentation.componants.newsletter.NewsletterCard
import com.xpeho.xpeapp.ui.presentation.viewModel.newsletter.NewsletterViewModel
import com.xpeho.xpeapp.ui.presentation.componants.AppBar

@Composable
fun NewsletterPage(
    navigationController: NavController,
    newsletterViewModel: NewsletterViewModel = viewModel(),
    onBackPressed: () -> Unit,
) {
    val newsletters = newsletterViewModel.state.value
    val loading = newsletterViewModel.isLoading
    Scaffold(
        topBar = {
            AppBar(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(id = R.string.newsletters_page_title),
            ) {
                onBackPressed()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            items(newsletters) { newsletter ->
                Box(
                    modifier = Modifier
                        .clickable {
                            navigationController.navigate(
                                route = Screens.NewsletterDetail.name + "/${newsletter.id}",
                            )
                        }
                ) {
                    NewsletterCard(
                        newsletter = newsletter
                    )
                }
            }
        }
        if (loading.value) {
            AppLoader()
        }
    }
}

