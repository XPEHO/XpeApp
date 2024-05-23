package com.xpeho.xpeapp.ui.componants.qvst

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun QvstCampaignList(
    campaigns: List<QvstCampaign>,
    onCampaignClick: (QvstCampaign) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        items(campaigns.size) { index ->
            QvstCampaignItem(
                campaign = campaigns[index],
            ) {
                onCampaignClick(campaigns[index])
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QvstCampaignItem(
    campaign: QvstCampaign,
    onClick: () -> Unit = {}
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val endDate = LocalDate.parse(campaign.end_date, DateTimeFormatter.ISO_DATE)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.surfaceVariant else Color.White,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = campaign.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Box(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = com.xpeho.xpeapp.R.string.qvst_campaign_detail_theme,
                        campaign.theme.name
                    ),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Text(
                text = stringResource(
                    id = com.xpeho.xpeapp.R.string.qvst_campaign_detail_end_date,
                    endDate.format(dateFormatter)
                ),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .shadow(
                        elevation = 4.dp,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}
