package com.xpeho.xpeapp.domain

import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthenticationManagerTest {
    private lateinit var wordpressRepo: WordpressRepository
    private lateinit var datastorePref: DatastorePref
    private lateinit var authManager: AuthenticationManager
    private lateinit var firebaseService: FirebaseService

    @Before
    fun setUp() {
        wordpressRepo = mockk()
        datastorePref = mockk()
        firebaseService = mockk()
        coEvery { firebaseService.authenticate() } just runs

        //return@async AuthResult.Success(Unit)
        authManager = AuthenticationManager(wordpressRepo, datastorePref, firebaseService)
    }

    @Test
    fun login_withValidCredentials_setsAuthenticatedState() = runBlocking {
        val username = "username"
        val password = "password"
        val token = WordpressToken(
            token = "token",
            userEmail = "user_email",
            userNicename = "user_nicename",
            userDisplayName = "user_display_name",
        )
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.Success(
            token
        )
        coEvery { wordpressRepo.getUserId(username) } returns "userId"
        coEvery { datastorePref.setAuthData(any()) } returns Unit
        coEvery { datastorePref.setIsConnectedLeastOneTime(true) } returns Unit
        coEvery { datastorePref.setWasConnectedLastTime(true) } returns Unit
        coEvery { datastorePref.setUserId("userId") } returns Unit

        val result = authManager.login(username, password)

        assert(result is AuthResult.Success, { "Result should be an AuthResult.Success" })
        assert(authManager.authState.value is AuthState.Authenticated, { "AuthState should be Authenticated" })
        coVerify { datastorePref.setAuthData(any()) }
    }

    @Test
    fun login_withInvalidCredentials_returnsUnauthorized() = runBlocking {
        val username = "username"
        val password = "password"
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.Unauthorized

        val result = authManager.login(username, password)

        assert(result is AuthResult.Unauthorized, { "Result should be an AuthResult.Unauthorized" })
        assert(authManager.authState.value is AuthState.Unauthenticated, { "AuthState should be Unauthenticated" })
    }

    @Test
    fun login_withNetworkError_returnsNetworkError() = runBlocking {
        val username = "username"
        val password = "password"
        coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.NetworkError

        val result = authManager.login(username, password)

        assert(result is AuthResult.NetworkError, { "Result should be an AuthResult.NetworkError" })
        assert(authManager.authState.value is AuthState.Unauthenticated, { "AuthState should be Unauthenticated" })
    }

}