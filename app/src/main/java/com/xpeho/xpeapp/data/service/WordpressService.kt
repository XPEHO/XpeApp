package com.xpeho.xpeapp.data.service

import com.google.gson.GsonBuilder
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val baseUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/api/"
//                           http://yaki.uat.xpeho.fr:7830/wp-json/api/v1/token

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
    .baseUrl(baseUrl)
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
