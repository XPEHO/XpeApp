package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.components.AppLoader
import com.xpeho.xpeapp.ui.components.layout.TitleWithFilter
import com.xpeho.xpeapp.ui.components.newsletter.NewsletterCardList
import com.xpeho.xpeapp.ui.viewModel.newsletter.NewsletterViewModel
import java.time.LocalDate

@Composable
fun NewsletterPage(
    newsletterViewModel: NewsletterViewModel = viewModel()
) {
    val classifiedNewsletters = newsletterViewModel.getClassifiedNewsletters()
    val loading = newsletterViewModel.isLoading
    // We set the selected year to the current year
    val currentYear = LocalDate.now().year
    val selectedYear = remember { mutableIntStateOf(currentYear) }

    LaunchedEffect(Unit) {
        newsletterViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        // Get the list of years
        var filterList = classifiedNewsletters.keys.toList()
        // Add the current year to the list if needed
        if (!filterList.contains(currentYear)) {
            filterList = filterList + currentYear
        }
        // Sort the list in descending order
        filterList = filterList.sortedDescending()

        // Display the content of the page
        item {
            TitleWithFilter<Int>(
                title = "Newsletters de l'année",
                filterList = filterList,
                state = selectedYear,
            )
            // Display the list of newsletters
            val newsletters: List<Newsletter> = classifiedNewsletters[selectedYear.intValue] ?: emptyList()
            NewsletterCardList(newsletters = newsletters)
        }
    }
    if (loading.value) {
        AppLoader()
    }
}
