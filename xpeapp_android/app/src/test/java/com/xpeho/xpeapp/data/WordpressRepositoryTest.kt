package com.xpeho.xpeapp.data

import android.util.Log
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.data.service.WordpressService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class WordpressRepositoryTest {
    private lateinit var wordpressRepo: WordpressRepository
    private lateinit var wordpressService: WordpressService

    @Before
    fun setUp() {
        wordpressService = mockk()
        wordpressRepo = WordpressRepository(wordpressService)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun authenticate_withValidCredentials_returnsSuccess() = runBlocking {
        val credentials = AuthentificationBody("username", "password")
        val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
        coEvery { wordpressService.authentification(credentials) } returns token

        val result = wordpressRepo.authenticate(credentials)

        assertEquals(AuthResult.Success(token), result)
    }

    @Test
    fun authenticate_withNetworkError_returnsNetworkError() = runBlocking {
        val credentials = AuthentificationBody("username", "password")
        coEvery { wordpressService.authentification(any()) } throws UnknownHostException()

        val result = wordpressRepo.authenticate(credentials)

        assertEquals(AuthResult.NetworkError, result)
    }

    @Test
    fun authenticate_withHttpException_returnsUnauthorized() = runBlocking {
        val credentials = AuthentificationBody("username", "password")
        coEvery { wordpressService.authentification(credentials) } throws HttpException(mockk {
            coEvery { code() } returns 403
            coEvery { message() } returns "message"
        })

        val result = wordpressRepo.authenticate(credentials)

        assertEquals(AuthResult.Unauthorized, result)
    }

    @Test
    fun validateToken_withValidToken_returnsSuccess() = runBlocking {
        val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
        coEvery { wordpressService.validateToken("Bearer ${token.token}") } returns Unit

        val result = wordpressRepo.validateToken(token)

        assertEquals(AuthResult.Success(Unit), result)
    }

    @Test
    fun getUserId_withValidUsername_returnsUserId() = runBlocking {
        val username = "username"
        coEvery { wordpressService.getUserId(username) } returns "userId"

        val result = wordpressRepo.getUserId(username)

        assertEquals("userId", result)
    }

    @Test
    fun getUserId_withNetworkError_returnsNull() = runBlocking {
        val username = "username"
        coEvery { wordpressService.getUserId(username) } throws ConnectException()

        val result = wordpressRepo.getUserId(username)

        assertEquals(null, result)
    }
}