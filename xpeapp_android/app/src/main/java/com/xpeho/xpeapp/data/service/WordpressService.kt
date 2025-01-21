package com.xpeho.xpeapp.data.service

import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.entity.QvstAnswerBody
import com.xpeho.xpeapp.data.model.UserInfos
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstProgress
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WordpressService {
    @Headers("Content-Type: application/json")
    @POST("jwt-auth/v1/token")
    suspend fun authentification(
        @Body body: AuthentificationBody,
    ): WordpressToken

    @Headers("Content-Type: application/json")
    @POST("jwt-auth/v1/token/validate")
    suspend fun validateToken(
        @Header("Authorization") token: String,
    )

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/user")
    suspend fun getUserId(
        @Header("email") username: String,
    ): String

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/qvst/campaigns{active}")
    suspend fun getQvstCampaigns(
        @Path("active") active: String = "",
    ): List<QvstCampaign>

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/qvst/campaigns/{campaignId}/questions")
    suspend fun getQvstQuestionsByCampaignId(
        @Path("campaignId") campaignId: String,
        @Header("userId") userId: String,
    ): List<QvstQuestion>

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/campaign-progress")
    suspend fun getQvstProgressByUserId(
        @Query("userId") userId: String,
    ): List<QvstProgress>

    @Headers("Content-Type: application/json")
    @POST("xpeho/v1/qvst/campaigns/{campaignId}/questions:answer")
    suspend fun submitAnswers(
        @Path("campaignId") campaignId: String,
        @Header("userId") userId: String,
        @Body answers: List<QvstAnswerBody>,
    ): Boolean

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/user-infos")
    suspend fun fetchUserInfos(): UserInfos
}

