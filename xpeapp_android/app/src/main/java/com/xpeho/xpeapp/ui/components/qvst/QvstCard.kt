package com.xpeho.xpeapp.ui.components.qvst

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

@Composable
fun QvstCard(
    navigationController: NavController,
    campaign: QvstCampaignEntity,
    collapsable: Boolean = true,
    defaultOpen: Boolean = false
) {
    CollapsableCard(
        label = campaign.name,
        tags = getTagsList(campaign = campaign),
        button =
            if ((campaign.status == "OPEN") && !campaign.completed) {
                {
                    key(campaign.id) {
                        ClickyButton(
                            label = "Compléter",
                            size = 14.sp,
                            verticalPadding = 3.dp,
                            horizontalPadding = 40.dp
                        ) {
                            navigationController.navigate(route = "${Screens.Qvst.name}/${campaign.id}")
                        }
                    }
                }
            } else {
                null
            }
        ,
        icon = {
            Icon(
                painter = painterResource(id = XpehoRes.qvst),
                contentDescription = "QVST Icon",
                tint =
                    if (campaign.status == "OPEN") XpehoColors.XPEHO_COLOR
                    else XpehoColors.GRAY_LIGHT_COLOR,
                modifier = Modifier
                    .size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = collapsable,
        defaultOpen = defaultOpen
    )
}

// Get the tag pills for a campaign
@Composable
private fun getTagsList(campaign: QvstCampaignEntity): @Composable() (() -> Unit) {

    // Init the tagsList depending the data that we got
    val tagPillTheme: @Composable() () -> Unit = {
        TagPill(
            label = campaign.themeName,
            backgroundColor = XpehoColors.GREEN_DARK_COLOR,
            size = 9.sp
        )
    }

    var tagPillDeadline: @Composable() () -> Unit

    // If the campaign is open we indicate the days remaining
    //  If it end in less or equal than 3 days and it hasn't been completed -> the color is red
    // Else we indicate the end date
    if (campaign.status == "OPEN") {
        tagPillDeadline = {
            TagPill(
                label =
                if (campaign.remainingDays > 0)
                    campaign.remainingDays.toString() + " jours restants"
                else
                    "Dernier jour",
                backgroundColor =
                if (campaign.completed || (campaign.remainingDays > 3))
                    XpehoColors.GREEN_DARK_COLOR
                else
                    XpehoColors.RED_INFO_COLOR,
                size = 9.sp
            )
        }
    } else {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val endDate = LocalDate.parse(campaign.endDate, DateTimeFormatter.ISO_DATE)
        tagPillDeadline = {
            TagPill(
                label = formatter.format(endDate),
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp
            )
        }
    }

    val tagPillCompletion: @Composable() () -> Unit

    // If campaign is completed we indicate that it has been complete
    // Else if it is always open we indicate it can always be completed
    if (campaign.completed) {
        tagPillCompletion = {
            TagPill(
                label = "Complétée",
                backgroundColor = XpehoColors.XPEHO_COLOR,
                size = 9.sp
            )
        }
    } else if (campaign.status == "OPEN") {
        tagPillCompletion = {
            TagPill(
                label = "À compléter",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp
            )
        }
    } else {
        tagPillCompletion = {}
    }

    return {
        tagPillTheme.invoke()
        tagPillDeadline.invoke()
        tagPillCompletion.invoke()
    }
}