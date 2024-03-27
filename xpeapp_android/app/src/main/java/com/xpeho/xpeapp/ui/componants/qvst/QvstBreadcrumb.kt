package com.xpeho.xpeapp.ui.componants.qvst

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstBreadcrumbUiState
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstBreadcrumbViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.theme.XpeAppTheme

// Stateful composable

@Composable
fun QvstBreadcrumb(viewModel: QvstBreadcrumbViewModel = viewModel(), modifier: Modifier = Modifier) {
    val uiState = viewModel.uiState.collectAsState()
    QvstBreadcrumb(uiState.value, modifier)
}

// Stateless composable

@Composable
fun QvstBreadcrumb(uiState: QvstBreadcrumbUiState, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        when (uiState) {
            is QvstBreadcrumbUiState.LOADING -> {
                LoadingQvstBreadcrumb(Modifier.fillMaxWidth())
            }

            is QvstBreadcrumbUiState.SUCCESS -> {
                SuccessQvstBreadcrumb(uiState, Modifier.fillMaxWidth())
            }

            is QvstBreadcrumbUiState.ERROR -> {
                ErrorQvstBreadcrumb(uiState, Modifier.fillMaxWidth())
            }

            is QvstBreadcrumbUiState.NO_CURRENT_CAMPAIGN -> {}
        }
    }
}

@Composable
fun LoadingQvstBreadcrumb(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.chargement),
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

private enum class DayRange {
    UNTIL_7,
    UNTIL_14,
    MORE_THAN_14;

    @Composable
    fun backgroundColor(): Color {
        return when (this) {
            UNTIL_7 -> Color.Red
            UNTIL_14 -> Color.Yellow
            MORE_THAN_14 -> MaterialTheme.colorScheme.primary
        }
    }

    @Composable
    fun textColor(): Color {
        return when (this) {
            UNTIL_7 -> Color.Black
            UNTIL_14 -> Color.Black
            MORE_THAN_14 -> MaterialTheme.colorScheme.onPrimary
        }
    }
}

private fun Long.toDayRange(): DayRange {
    return when {
        this <= 7 -> DayRange.UNTIL_7
        this <= 14 -> DayRange.UNTIL_14
        else -> DayRange.MORE_THAN_14
    }
}

@Composable
fun SuccessQvstBreadcrumb(uiState: QvstBreadcrumbUiState.SUCCESS, modifier: Modifier = Modifier) {
    val remainingDays = uiState.remainingDays
    val campaignName = uiState.campaignName
    val range = remainingDays.toDayRange()
    val backgroundColor: Color = range.backgroundColor()
    val textColor: Color = range.textColor()
    val breadcrumbPadding = 2.dp
    Text(
        text = stringResource(R.string.jours_restants, campaignName, remainingDays.toString()),
        modifier = modifier
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(breadcrumbPadding),
        textAlign = TextAlign.Center,
        color = textColor
    )
}

@Composable
fun ErrorQvstBreadcrumb(uiState: QvstBreadcrumbUiState.ERROR, modifier: Modifier = Modifier) {
    val errMessage = uiState.message
    Text(
        text = stringResource(R.string.erreur_jours_campagne, errMessage),
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

// Previews

@Preview
@Composable
fun PreviewQvstBreadcrumb10d() {
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne C", 10)
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewQvstBreadcrumb5d() {
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne B", 5)
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewQvstBreadcrum20d() {
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne A", 20)
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewLoadingQvstBreadcrumb() {
    val uiState = QvstBreadcrumbUiState.LOADING
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewErrorQvstBreadcrumb() {
    val uiState = QvstBreadcrumbUiState.ERROR("API Error")
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewNoCurrentCampaignQvstBreadcrumb() {
    val uiState = QvstBreadcrumbUiState.NO_CURRENT_CAMPAIGN
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}