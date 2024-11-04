package com.xpeho.xpeapp.ui.page.qvst

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.ListFilter
import com.xpeho.xpeapp.ui.components.layout.ListFilterTitle
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.components.qvst.QvstCardList
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import java.time.LocalDate

@Composable
fun QvstPage(
    navigationController: NavController,
) {

    // We set the selected year to the current year
    val currentYear = LocalDate.now().year
    var selectedYear by remember { mutableStateOf(currentYear) }

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
                // Get the list of campaigns classified by year
                val classifiedCampaigns = (campaignViewModel.state as QvstUiState.SUCCESS).qvst
                // Get the list of years
                var filterList = classifiedCampaigns.keys.toList()
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
                        Title(label = "Campagnes de l'année")
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
                    val campaigns: List<QvstCampaignEntity> = classifiedCampaigns[selectedYear] ?: emptyList()
                    QvstCardList(
                        navigationController = navigationController,
                        campaigns = campaigns,
                        collapsable = true
                    )
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