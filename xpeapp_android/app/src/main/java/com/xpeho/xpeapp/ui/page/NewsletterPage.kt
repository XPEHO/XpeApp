package com.xpeho.xpeapp.ui.page

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.ui.components.AppLoader
import com.xpeho.xpeapp.ui.components.newsletter.NewsletterCard
import com.xpeho.xpeapp.ui.viewModel.newsletter.NewsletterViewModel

@Composable
fun NewsletterPage(
    newsletterViewModel: NewsletterViewModel = viewModel()
) {
    val newsletters = newsletterViewModel.state.value
    val loading = newsletterViewModel.isLoading

    LaunchedEffect(Unit) {
        newsletterViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        items(newsletters) { newsletter ->
            NewsletterCard(
                newsletter = newsletter,
                open = newsletter == newsletters.first()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    if (loading.value) {
        AppLoader()
    }
}
