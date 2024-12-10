package com.xpeho.xpeapp.data.service

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.entity.QvstAnswerBody
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstProgress
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.net.ssl.SSLHandshakeException

class WordpressRepository(
    private val api: WordpressService,
) {

    companion object {
        private const val HTTPFORBIDDEN = 403

        private const val DATETIME_FORMATTER_PATTERN = "yyyy-MM-dd"
    }

    suspend fun authenticate(credentials: AuthentificationBody): AuthResult<WordpressToken> {
        handleServiceExceptions(
            tryBody = {
                val token = api.authentification(credentials)
                return AuthResult.Success(token)
            },
            catchBody = { e ->
                return handleAuthExceptions(e)
            }
        )
    }

    suspend fun validateToken(token: WordpressToken): AuthResult<Unit> {
        handleServiceExceptions(
            tryBody = {
                api.validateToken("Bearer ${token.token}")
                return AuthResult.Success(Unit)
            },
            catchBody = { e ->
                return handleAuthExceptions(e)
            }
        )
    }

    suspend fun getUserId(username: String): String? {
        handleServiceExceptions(
            tryBody = {
                return api.getUserId(username)
            },
            catchBody = { e ->
                Log.e("WordpressRepository: getUserId", "Network error: ${e.message}")
                return null
            }
        )
    }

    fun classifyCampaigns(campaigns: List<QvstCampaignEntity>): Map<Int, List<QvstCampaignEntity>> {
        return campaigns.groupBy { campaign ->
            val formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER_PATTERN)
            val endDate = LocalDate.parse(campaign.endDate, formatter)
            val year = endDate.year
            year
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCampaignsEntitiesFromModels(
        campaigns: List<QvstCampaign>,
        progress: List<QvstProgress>
    ): List<QvstCampaignEntity> {
        val campaignsEntities = campaigns
            .filter { campaign -> campaign.status != "DRAFT" }
            .map { campaign -> getCampaignEntityFromModel(campaign, progress) }
        return campaignsEntities.sortedByDescending { it.endDate }
    }

    private fun getCampaignEntityFromModel(
        campaign: QvstCampaign,
        progress: List<QvstProgress>
    ): QvstCampaignEntity {
        var remainingDays = countDaysBetween(getTodayDateString(), campaign.endDate)
        if (remainingDays < 0) {
            remainingDays = 0
        }

        val campaignProgress = progress.firstOrNull { it.campaignId == campaign.id }

        val completed = campaignProgress?.let {
            it.answeredQuestions >= it.totalQuestions
        } ?: false

        return QvstCampaignEntity(
            id = campaign.id,
            name = campaign.name,
            themeName = campaign.theme.name,
            status = campaign.status,
            outdated = remainingDays <= 0,
            completed = completed,
            remainingDays = remainingDays.toInt(),
            endDate = campaign.endDate,
            resultLink = campaign.action
        )
    }

    suspend fun getQvstCampaigns(
        username: String,
        onlyActive: Boolean = false
    ): List<QvstCampaignEntity>? {
        handleServiceExceptions(
            tryBody = {
                val campaigns = if (onlyActive) {
                    api.getQvstCampaigns(":active")
                } else {
                    api.getQvstCampaigns()
                }
                val userId = getUserId(username)
                val progress = userId?.let {
                    api.getQvstProgressByUserId(userId)
                }
                return progress?.let {
                    getCampaignsEntitiesFromModels(campaigns, progress)
                }
            },
            catchBody = { e ->
                if (e is HttpException && e.code() == 403) {
                    Log.e("WordpressRepository: getAllQvstCampaigns", "HTTP 403 Forbidden: ${e.message}")
                    return emptyList()
                }
                Log.e("WordpressRepository: getAllQvstCampaigns", "Network error: ${e.message}")
                return null
            }
        )
    }

    suspend fun getQvstQuestionsByCampaignId(
        campaignId: String,
        userId: String,
    ): List<QvstQuestion>? {
        handleServiceExceptions(
            tryBody = {
                return api.getQvstQuestionsByCampaignId(
                    campaignId = campaignId,
                    userId = userId,
                )
            },
            catchBody = { e ->
                if (e is HttpException && e.code() == 403) {
                    Log.e("WordpressRepository: getQvstQuestionsByCampaignId", "HTTP 403 Forbidden: ${e.message}")
                    return emptyList()
                }
                Log.e("WordpressRepository: getQvstQuestionsByCampaignId", "Network error: ${e.message}")
                return null
            }
        )
    }

    suspend fun submitAnswers(
        campaignId: String,
        userId: String,
        answers: List<QvstAnswerBody>,
    ): Boolean {
        handleServiceExceptions(
            tryBody = {
                api.submitAnswers(
                    campaignId = campaignId,
                    userId = userId,
                    answers = answers,
                )
                return true
            },
            catchBody = { e ->
                Log.e("WordpressRepository: submitAnswers", "Network error: ${e.message}")
                return false
            }
        )
    }

    // Exceptions handling

    @Suppress("ReturnCount")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleAuthExceptions(e: Exception): AuthResult<Nothing> {
        // Backend sends a 403 Forbidden
        if (e is HttpException && e.code() == HTTPFORBIDDEN) {
            Log.e("WordpressRepository", "Unauthorized error: ${e.message}")
            return AuthResult.Unauthorized
        }

        // Check if it's a network error
        if (isNetworkError(e)) {
            Log.e("WordpressRepository", "Network error: ${e.message}")
            return AuthResult.NetworkError
        }

        // Unknown error
        Log.e("WordpressRepository", "Unknown error: ${e.message}")
        throw e
    }

    fun isNetworkError(e: Exception): Boolean {
        return when (e) {
            is UnknownHostException -> true
            is SocketTimeoutException -> true
            is SSLHandshakeException -> true
            is ConnectException -> true
            is HttpException -> true
            else -> false
        }
    }

    @Suppress("TooGenericExceptionCaught")
    inline fun <T> handleServiceExceptions(
        tryBody: () -> T,
        catchBody: (Exception) -> T
    ): T {
        return try {
            tryBody()
        } catch (e: Exception) {
            if (isNetworkError(e)) {
                catchBody(e)
            } else {
                throw e
            }
        }
    }

    // Utils date methods
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun countDaysBetween(
        startDateStr: String,
        endDateStr: String,
        pattern: String = DATETIME_FORMATTER_PATTERN
    ): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getTodayDateString(pattern: String = DATETIME_FORMATTER_PATTERN): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return today.format(formatter)
    }
}

