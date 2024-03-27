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

private object RemDaysBounds {
    const val LOW = 0
    const val MEDIUM = 7
    const val HIGH = 14
}

@Composable
fun SuccessQvstBreadcrumb(uiState: QvstBreadcrumbUiState.SUCCESS, modifier: Modifier = Modifier) {
    val remainingDays = uiState.remainingDays
    val campaignName = uiState.campaignName

    val backgroundColor: Color = when (remainingDays) {
        in RemDaysBounds.LOW..RemDaysBounds.MEDIUM -> Color.Red
        in RemDaysBounds.MEDIUM..RemDaysBounds.HIGH -> Color.Yellow
        else -> MaterialTheme.colorScheme.primary
    }
    val textColor: Color = when (remainingDays) {
        in RemDaysBounds.LOW..RemDaysBounds.MEDIUM -> Color.Black
        in RemDaysBounds.MEDIUM..RemDaysBounds.HIGH -> Color.Black
        else -> MaterialTheme.colorScheme.onPrimary
    }

    Text(
        text = stringResource(R.string.jours_restants, campaignName, remainingDays.toString()),
        modifier = modifier
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(2.dp),
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
    val days = 10L
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne C", days)
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewQvstBreadcrumb5d() {
    val days = 5L
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne B", days)
    XpeAppTheme {
        QvstBreadcrumb(uiState)
    }
}

@Preview
@Composable
fun PreviewQvstBreadcrum20d() {
    val days = 20L
    val uiState = QvstBreadcrumbUiState.SUCCESS("Campagne A", days)
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