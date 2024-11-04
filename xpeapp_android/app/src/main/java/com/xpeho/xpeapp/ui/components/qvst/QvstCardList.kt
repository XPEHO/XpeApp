package com.xpeho.xpeapp.ui.components.qvst

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder

@Composable
fun QvstCardList(
    navigationController: NavController,
    campaigns: List<QvstCampaignEntity>,
    collapsable: Boolean = true
) {
    if (campaigns.isEmpty()) {
        // Display a placeholder if there is no campaign
        NoContentPlaceHolder()
    } else {
        // Display the list of campaigns
        Column {
            for (campaign in campaigns) {
                QvstCard(
                    navigationController = navigationController,
                    campaign = campaign,
                    collapsable = collapsable,
                    defaultOpen = !collapsable || campaign.status == "OPEN"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}