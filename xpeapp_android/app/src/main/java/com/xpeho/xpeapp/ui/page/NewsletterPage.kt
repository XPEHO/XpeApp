package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.components.AppLoader
import com.xpeho.xpeapp.ui.components.layout.ListFilter
import com.xpeho.xpeapp.ui.components.layout.ListFilterTitle
import com.xpeho.xpeapp.ui.components.layout.Title
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
    var selectedYear by remember { mutableStateOf(currentYear) }

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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display the title
                Title(label = "Newsletters de l'année")
                // Display the filter if there is more than one year in the list
                if (filterList.size > 1) {
                    ListFilter<Int>(
                        elements = filterList,
                        defaultSelectedElement = selectedYear,
                    ) { selectedElement ->
                        selectedYear = selectedElement
                    }
                } else {
                    ListFilterTitle(label = selectedYear.toString())
                }
            }
            // Display the list of newsletters
            val newsletters: List<Newsletter> = classifiedNewsletters[selectedYear] ?: emptyList()
            NewsletterCardList(newsletters = newsletters)
        }
    }
    if (loading.value) {
        AppLoader()
    }
}
