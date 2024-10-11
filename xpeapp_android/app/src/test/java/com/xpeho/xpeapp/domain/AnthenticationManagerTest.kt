package com.xpeho.xpeapp.domain

import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.WordpressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class AuthenticationManagerTest {
    private lateinit var wordpressRepo: WordpressRepository
    private lateinit var datastorePref: DatastorePref
    private lateinit var authManager: AuthenticationManager

    @Before
    fun setUp() {
        wordpressRepo = mockk()
        datastorePref = mockk()
        authManager = AuthenticationManager(wordpressRepo, datastorePref)
    }

    @Test
    fun login_withValidCredentials_setsAuthenticatedState() = runBlocking {
        val username = "username"
        val password = "password"
        val token = WordpressToken(
            token = "token",
            user_email = "user_email",
            user_nicename = "user_nicename",
            user_display_name = "user_display_name",
        )
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.Success(token)
        coEvery { wordpressRepo.getUserId(username) } returns "userId"
        coEvery { datastorePref.setAuthData(any()) } returns Unit
        coEvery { datastorePref.setIsConnectedLeastOneTime(true) } returns Unit
        coEvery { datastorePref.setWasConnectedLastTime(true) } returns Unit
        coEvery { datastorePref.setUserId("userId") } returns Unit

        val result = authManager.login(username, password)

        assertThat("Result should be an AuthResult.Success", result is AuthResult.Success)
        assertThat("AuthState should be Authenticated", authManager.authState.value is AuthState.Authenticated)
        coVerify { datastorePref.setAuthData(any()) }
    }

    @Test
    fun login_withInvalidCredentials_returnsUnauthorized() = runBlocking {
        val username = "username"
        val password = "password"
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.Unauthorized

        val result = authManager.login(username, password)

        assertThat("Result should be an AuthResult.Unauthorized", result is AuthResult.Unauthorized)
        assertThat("AuthState should be Unauthenticated", authManager.authState.value is AuthState.Unauthenticated)
    }

    @Test
    fun login_withNetworkError_returnsNetworkError() = runBlocking {
        val username = "username"
        val password = "password"
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.NetworkError

        val result = authManager.login(username, password)

        assertThat("Result should be an AuthResult.NetworkError", result is AuthResult.NetworkError)
        assertThat("AuthState should be Unauthenticated", authManager.authState.value is AuthState.Unauthenticated)
    }

}