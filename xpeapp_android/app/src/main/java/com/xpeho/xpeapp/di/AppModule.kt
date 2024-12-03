package com.xpeho.xpeapp.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.xpeho.xpeapp.BuildConfig
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.data.service.WordpressService
import com.xpeho.xpeapp.data.service.interceptor.AuthorizationHeaderInterceptor
import com.xpeho.xpeapp.domain.AuthenticationManager
import com.xpeho.xpeapp.domain.FeatureFlippingManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val authenticationManager: AuthenticationManager
    val featureFlippingManager: FeatureFlippingManager
    val wordpressRepository: WordpressRepository
    val datastorePref: DatastorePref
}

class MainAppModule(
    private val appContext: Context
) : AppModule {


    private val baseUrl = BuildConfig.BACKEND_URL

    private val gson = GsonBuilder().setLenient().create()

    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val authorization by lazy {
        AuthorizationHeaderInterceptor()
    }

    private val okHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(authorization)
            .addInterceptor(logging)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
    }

    private val wordpressService: WordpressService by lazy {
        retrofit.create(WordpressService::class.java)
    }

    private val firebaseService: FirebaseService by lazy {
        FirebaseService()
    }

    override val authenticationManager: AuthenticationManager by lazy {
        AuthenticationManager(
            wordpressRepo = wordpressRepository,
            datastorePref = datastorePref,
            firebaseService = firebaseService
        )
    }

    override val featureFlippingManager: FeatureFlippingManager by lazy {
        FeatureFlippingManager(
            firebaseService = firebaseService
        )
    }

    override val wordpressRepository: WordpressRepository by lazy {
        WordpressRepository(api = wordpressService)
    }


    override val datastorePref: DatastorePref by lazy {
        DatastorePref(appContext)
    }
}


