package com.xpeho.xpeapp.data.service.interceptor

import android.util.Log
import com.xpeho.xpeapp.di.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationHeaderInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.get()
        val hasAuthorizationHeader = chain.request().headers("Authorization").isNotEmpty()

        if (hasAuthorizationHeader) {
            return identityResponse(chain)
        }
        return when (token) {
            null -> identityResponse(chain)
            else -> authorizedResponse(chain, token)
        }
    }

    private fun authorizedResponse(chain: Interceptor.Chain, token: String): Response {
        Log.i("AuthorizationHeaderInterceptor", "Request was sent with the added bearer token $token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(request)
    }

    private fun identityResponse(chain: Interceptor.Chain): Response {
        Log.i("AuthorizationHeaderInterceptor", "Request was sent witout an added bearer token")
        return chain.proceed(chain.request())
    }
}