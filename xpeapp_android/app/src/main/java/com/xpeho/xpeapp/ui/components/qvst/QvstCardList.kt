package com.xpeho.xpeapp.ui.components.qvst

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign

@Composable
fun QvstCardList(
    navigationController: NavController,
    campaigns: List<QvstCampaignEntity>,
    collapsable: Boolean = true
) {
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