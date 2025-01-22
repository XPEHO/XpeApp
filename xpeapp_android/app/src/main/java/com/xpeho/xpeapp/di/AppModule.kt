package com.xpeho.xpeapp.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
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
    val firebaseService: FirebaseService
    val wordpressRepository: WordpressRepository
    val datastorePref: DatastorePref
    val tokenProvider: TokenProvider
    val firebaseAnalytics: FirebaseAnalytics
}

class MainAppModule(
    private val appContext: Context
) : AppModule {


    private val baseUrl = BuildConfig.BACKEND_URL

    private val gson = GsonBuilder().setLenient().create()

    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private val authorization by lazy {
        AuthorizationHeaderInterceptor(
            tokenProvider = tokenProvider
        )
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

    override val firebaseService: FirebaseService by lazy {
        FirebaseService()
    }

    override val tokenProvider: TokenProvider by lazy {
        TokenProvider()
    }
    override val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(appContext).apply {
            setAnalyticsCollectionEnabled(true)
        }
    }

    override val authenticationManager: AuthenticationManager by lazy {
        AuthenticationManager(
            tokenProvider = tokenProvider,
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
        DatastorePref(
            appContext,
            tokenProvider
        )
    }
}


