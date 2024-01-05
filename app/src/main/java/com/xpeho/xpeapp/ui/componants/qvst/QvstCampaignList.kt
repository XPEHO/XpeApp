package com.xpeho.xpeapp.ui.componants.qvst

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
                color = androidx.compose.ui.graphics.Color.White,
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
                    )
                )
                Box(modifier = Modifier.height(8.dp))
                Text(
                    text = "Thème : ${campaign.theme.name}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                )
            }
            Text(
                text = "Fin le ${endDate.format(dateFormatter)}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
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

@Preview
@Composable
fun QvstCampaignListPreview() {
    QvstCampaignItem(
        campaign = QvstCampaign(
            id = "1",
            name = "Campagne 1",
            theme = QvstTheme(id = "1", name = "Thème 1"),
            status = "OPEN",
            start_date = "2021-09-01",
            end_date = "2021-09-30",
            participation_rate = "0"
        ),
    )
}
