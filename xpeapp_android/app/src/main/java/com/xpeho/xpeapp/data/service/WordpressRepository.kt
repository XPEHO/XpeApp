package com.xpeho.xpeapp.data.service

import android.util.Log
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

class WordpressRepository(private val api: WordpressService) {

    companion object {
        private const val HTTPFORBIDDEN = 403
        private const val HTTPSERVICEUNAVAILABLE = 503

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

    fun classifyCampaigns(campaigns: ArrayList<QvstCampaignEntity>): Map<String, List<QvstCampaignEntity>> {
        return campaigns.groupBy { campaign ->
            if (campaign.status == "OPEN") {
                "open"
            } else {
                val formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER_PATTERN)
                val endDate = LocalDate.parse(campaign.endDate, formatter)
                val year = endDate.year
                year.toString()
            }
        }
    }

    private fun getCampaignsEntitiesFromModels(
        campaigns: List<QvstCampaign>,
        progress: List<QvstProgress>
    ): ArrayList<QvstCampaignEntity> {
        val campaignsEntities = ArrayList<QvstCampaignEntity>()

        for (campaign in campaigns) {
            var remainingDays = countDaysBetween(getTodayDateString(), campaign.endDate)
            if (remainingDays < 0)
                remainingDays = 0

            val campaignProgress = progress.firstOrNull { it.campaignId == campaign.id }

            val completed = campaignProgress?.let {
                it.answeredQuestions >= it.totalQuestions
            } ?: false

            campaignsEntities.add(
                QvstCampaignEntity(
                    id = campaign.id,
                    name = campaign.name,
                    themeName = campaign.theme.name,
                    status = campaign.status,
                    outdated = remainingDays <= 0,
                    completed = completed,
                    remainingDays = remainingDays.toInt(),
                    endDate = campaign.endDate
                )
            )
        }

        return campaignsEntities
    }

    suspend fun getAllQvstCampaigns(
        token: WordpressToken,
        username: String
    ): ArrayList<QvstCampaignEntity>? {
        handleServiceExceptions(
            tryBody = {
                val campaigns = api.getAllQvstCampaigns()
                val userId = getUserId(username = username)
                val progress = userId?.let {
                    api.getQvstProgressByUserId(
                        token = "Bearer ${token.token}",
                        userId = userId
                    )
                    api.getQvstProgressByUserId(
                        token = "Bearer ${token.token}",
                        userId = userId
                    )
                }

                return progress?.let {
                    getCampaignsEntitiesFromModels(campaigns, progress)
                }
            },
            catchBody = { e ->
                Log.e("WordpressRepository: getAllQvstCampaigns", "Network error: ${e.message}")
                return null
            }
        )
    }

    suspend fun getActiveQvstCampaigns(
        token: WordpressToken,
        username: String
    ): ArrayList<QvstCampaignEntity>? {
        handleServiceExceptions(
            tryBody = {
                val campaigns = api.getActiveQvstCampaigns()
                val userId = getUserId(username = username)
                val progress = userId?.let {
                    api.getQvstProgressByUserId(
                        token = "Bearer ${token.token}",
                        userId = userId
                    )
                    api.getQvstProgressByUserId(
                        token = "Bearer ${token.token}",
                        userId = userId
                    )
                }

                return progress?.let {
                    getCampaignsEntitiesFromModels(campaigns, progress)
                }
            },
            catchBody = { e ->
                Log.e("WordpressRepository: getActiveQvstCampaigns", "Network error: ${e.message}")
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
    private fun handleAuthExceptions(e: Exception): AuthResult<Nothing> {
        // Check if it's a network error
        if (isNetworkError(e)) {
            Log.e("WordpressRepository", "Network error: ${e.message}")
            return AuthResult.NetworkError
        }

        // Backend sends a 403 Forbidden
        if (e is HttpException && e.code() == HTTPFORBIDDEN) {
            Log.e("WordpressRepository", "Unauthorized error: ${e.message}")
            return AuthResult.Unauthorized
        }

        // Unknown error
        Log.e("WordpressRepository", "Unknown error: ${e.message}")
        throw e
    }

    private fun isNetworkError(e: Exception): Boolean {
        return when (e) {
            is UnknownHostException -> true
            is SocketTimeoutException -> true
            is SSLHandshakeException -> true
            is ConnectException -> true
            is HttpException -> e.code() == HTTPSERVICEUNAVAILABLE
            else -> false
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private inline fun <T> handleServiceExceptions(
        tryBody: () -> T,
        catchBody: (Exception) -> T
    ): T {
        return try {
            tryBody()
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException,
                is SocketTimeoutException,
                is SSLHandshakeException,
                is ConnectException,
                is HttpException -> {
                    catchBody(e)
                }

                else -> throw e
            }
        }
    }

    // Utils date methods
    private fun countDaysBetween(
        startDateStr: String,
        endDateStr: String,
        pattern: String = DATETIME_FORMATTER_PATTERN
    ): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    private fun getTodayDateString(pattern: String = DATETIME_FORMATTER_PATTERN): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return today.format(formatter)
    }
}

