package com.xpeho.xpeapp.data.service

import com.google.gson.GsonBuilder
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = BuildConfig.BACKEND_URL

private val gson = GsonBuilder()
    .setLenient()
    .create()

val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface WordpressService {
    @Headers("Content-Type: application/json")
    @POST("v1/token")
    suspend fun authentification(
        @Body body: AuthentificationBody,
    ): WordpressToken
}

object WordpressAPI {
    val service: WordpressService by lazy {
        retrofit.create(WordpressService::class.java)
    }
}
