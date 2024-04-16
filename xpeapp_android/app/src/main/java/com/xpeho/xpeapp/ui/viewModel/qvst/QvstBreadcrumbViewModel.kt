package com.xpeho.xpeapp.ui.viewModel.qvst

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.service.WordpressAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class QvstBreadcrumbViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<QvstBreadcrumbUiState>(QvstBreadcrumbUiState.LOADING)
    val uiState: StateFlow<QvstBreadcrumbUiState> = _uiState

    init {
        getRemainingTime()
    }

    private fun getRemainingTime() {
        viewModelScope.launch {
            val campaigns = try {
                WordpressAPI.service.getAllQvstCampaigns()
            } catch (e: Exception) {
                _uiState.value = QvstBreadcrumbUiState.ERROR("API Error")
                return@launch
            }

            var currentCampaign: QvstCampaign? = campaigns.firstOrNull { _isCurrent(it) }

            for (campaign in campaigns) {
                if(_isCurrent(campaign)) {
                    currentCampaign = campaign
                    break
                }
            }

            Log.d("breadcrumb", "currentCampaign: $currentCampaign")

            if (currentCampaign == null) {
                _uiState.value = QvstBreadcrumbUiState.NO_CURRENT_CAMPAIGN
                return@launch
            }

            _uiState.value = QvstBreadcrumbUiState.SUCCESS(currentCampaign.name,
                _remainingDays(currentCampaign))
        }

    }

    private fun _isCurrent(campaign: QvstCampaign): Boolean {
        val startDateString = campaign.start_date
        val endDateString = campaign.end_date
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate= LocalDate.parse(startDateString, formatter)
        val endDate = LocalDate.parse(endDateString, formatter)
        return (currentDate.isAfter(startDate) && currentDate.isBefore(endDate))
                || currentDate.isEqual(startDate) || currentDate.isEqual(endDate)
    }

    private fun _remainingDays(campaign: QvstCampaign): Long {
        val endDate = campaign.end_date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val endDateParsed = LocalDate.parse(endDate, formatter)
        val currentDate = LocalDate.now()
        return endDateParsed.toEpochDay() - currentDate.toEpochDay() + 1
    }
}

sealed class QvstBreadcrumbUiState {
    object LOADING : QvstBreadcrumbUiState()
    data class SUCCESS(
        val campaignName: String,
        val remainingDays: Long
    ) : QvstBreadcrumbUiState()
    object NO_CURRENT_CAMPAIGN : QvstBreadcrumbUiState()
    data class ERROR(val message: String) : QvstBreadcrumbUiState()
}