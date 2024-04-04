package com.xpeho.xpeapp.data.service

import com.google.gson.GsonBuilder
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.entity.QvstAnswerBody
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.data.service.interceptor.AuthorizationHeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = BuildConfig.BACKEND_URL

private val gson = GsonBuilder().setLenient().create()

val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
val authorization = AuthorizationHeaderInterceptor(XpeApp.appModule.authenticationManager)

private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(authorization)
    .addInterceptor(logging)
    .build()

private val retrofit =
        Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()

interface WordpressService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/token")
    suspend fun authentification(
            @Body body: AuthentificationBody,
    ): WordpressToken

    @Headers("Content-Type: application/json")
    @GET("api/v1/token-validate")
    suspend fun validateToken(
            @Header("Authorization") token: String,
    )

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/user")
    suspend fun getUserId(
            @Header("email") username: String,
    ): String

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/qvst/campaigns")
    suspend fun getAllQvstCampaigns(): List<QvstCampaign>

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/qvst/campaigns:active")
    suspend fun getQvstCampaigns(): List<QvstCampaign>

    @Headers("Content-Type: application/json")
    @GET("xpeho/v1/qvst/campaigns/{campaignId}/questions")
    suspend fun getQvstQuestionsByCampaignId(
            @Path("campaignId") campaignId: String,
            @Header("userId") userId: String
    ): List<QvstQuestion>

    @Headers("Content-Type: application/json")
    @POST("xpeho/v1/qvst/campaigns/{campaignId}/questions:answer")
    suspend fun submitAnswers(
            @Path("campaignId") campaignId: String,
            @Header("userId") userId: String,
            @Body answers: List<QvstAnswerBody>,
    ): Boolean
}

object WordpressAPI {
    val service: WordpressService by lazy { retrofit.create(WordpressService::class.java) }
}
