package com.xpeho.xpeapp.presentation.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.presentation.componants.AppBar
import com.xpeho.xpeapp.presentation.componants.newsletter.NewsletterCard
import com.xpeho.xpeapp.presentation.viewModel.NewsletterViewModel

@Composable
fun NewsletterPage(
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
                NewsletterCard(newsletter = newsletter)
            }
        }
        if (loading.value) {
            Text(text = "Loading")
            /*CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp),
            )*/
        }
    }
}

