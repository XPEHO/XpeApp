package com.xpeho.xpeapp.data.service

import android.util.Log
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstProgress
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.net.ssl.SSLHandshakeException

class WordpressRepository {
    companion object {
        private const val HTTPBADREQUEST = 400
        private const val HTTPFORBIDDEN = 403
        private const val HTTPSERVICEUNAVAILABLE = 503
    }

    suspend fun authenticate(credentials: AuthentificationBody): AuthResult<WordpressToken> {
        return try {
            val token = WordpressAPI.service.authentification(credentials)
            AuthResult.Success(token)
        } catch (e: Exception) {
            handleAuthException(e)
        }
    }

    suspend fun validateToken(token: WordpressToken): AuthResult<Unit> {
        return try {
            WordpressAPI.service.validateToken("Bearer ${token.token}")
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            handleAuthException(e)
        }
    }

    suspend fun getUserId(username: String): String? {
        return try {
            WordpressAPI.service.getUserId(username)
        } catch (e: HttpException) {
            Log.e("WordpressRepository", "Unknown error: ${e.message}")
            null
        }
    }

    fun classifyCampaigns(campaigns: ArrayList<QvstCampaignEntity>): Map<String, List<QvstCampaignEntity>> {
        return campaigns.groupBy { campaign ->
            if (campaign.status == "OPEN") {
                "open"
            } else {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
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
        var campaignsEntities = ArrayList<QvstCampaignEntity>()

        for (campaign in campaigns) {
            var remainingDays = countDaysBetween(getTodayDateString(), campaign.end_date)
            if (remainingDays < 0)
                remainingDays = 0

            val campaignProgress = progress.firstOrNull { it.campaign_id == campaign.id }

            val completed = campaignProgress?.let {
                it.answered_questions >= it.total_questions
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
                    endDate = campaign.end_date
                )
            )
        }

        return campaignsEntities
    }

    suspend fun getAllQvstCampaigns(
        token: WordpressToken,
        username: String
    ): ArrayList<QvstCampaignEntity>? {
        try {
            val campaigns = WordpressAPI.service.getAllQvstCampaigns()
            val userId = getUserId(username = username)
            val progress = userId?.let {
                WordpressAPI.service.getQvstProgressByUserId(
                    token = "Bearer ${token.token}",
                    userId = userId
                )
            }

            return progress?.let {
                getCampaignsEntitiesFromModels(campaigns, progress)
            }

        } catch (e: Exception) {
            Log.e("WordpressRepository: getAllQvstCampaigns", "Unknown error: ${e.message}")
            return null
        }
    }

    suspend fun getActiveQvstCampaigns(
        token: WordpressToken,
        username: String
    ): ArrayList<QvstCampaignEntity>? {
        try {
            val campaigns = WordpressAPI.service.getActiveQvstCampaigns()
            val userId = getUserId(username = username)
            val progress = userId?.let {
                WordpressAPI.service.getQvstProgressByUserId(
                    token = "Bearer ${token.token}",
                    userId = userId
                )
            }

            return progress?.let {
                getCampaignsEntitiesFromModels(campaigns, progress)
            }

        } catch (e: Exception) {
            Log.e("WordpressRepository: getAllQvstCampaigns", "Unknown error: ${e.message}")
            return null
        }
    }

    private fun handleAuthException(e: Exception): AuthResult<Nothing> {
        if (isNetworkError(e)) {
            Log.e("WordpressRepository", "Network error: ${e.message}")
            return AuthResult.NetworkError
        }
        // Backend sends a 403 Forbidden
        if (e is HttpException && e.code() == HTTPFORBIDDEN) {
            Log.e("WordpressRepository", "Unauthorized error: ${e.message}")
            return AuthResult.Unauthorized
        }

        // Backend sends a 400 Bad Request, should send a 401 Unauthorized
        if (e is HttpException && e.code() == HTTPBADREQUEST)
            return AuthResult.Unauthorized
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

    // Utils date methods
    private fun countDaysBetween(
        startDateStr: String,
        endDateStr: String,
        pattern: String = "yyyy-MM-dd"
    ): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    private fun getTodayDateString(pattern: String = "yyyy-MM-dd"): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return today.format(formatter)
    }
}

