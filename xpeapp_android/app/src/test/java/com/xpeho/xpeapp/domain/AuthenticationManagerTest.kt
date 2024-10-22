package com.xpeho.xpeapp.domain

import android.util.Log
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.net.UnknownHostException

@RunWith(Enclosed::class)
class AuthenticationManagerTest {
    abstract class BaseTest {
        protected lateinit var wordpressRepo: WordpressRepository
        protected lateinit var datastorePref: DatastorePref
        protected lateinit var authManager: AuthenticationManager
        protected lateinit var firebaseService: FirebaseService

        @Before
        fun setUp() {
            wordpressRepo = mockk()
            datastorePref = mockk()
            firebaseService = mockk()
            coEvery { firebaseService.authenticate() } just runs

            //return@async AuthResult.Success(Unit)
            authManager = AuthenticationManager(wordpressRepo, datastorePref, firebaseService)

            // Mock android.util.Log methods
            mockkStatic(Log::class)
            every { Log.e(any(), any()) } returns 0
        }
    }

    class IsAuthValid : BaseTest() {
        @Test
        fun `isAuthValid returns false when unauthenticated`() = runBlocking {
            coEvery { datastorePref.getAuthData() } returns null

            authManager.restoreAuthStateFromStorage()
            val result = authManager.isAuthValid()

            assertFalse(result)
        }

        @Test
        fun `isAuthValid returns true when authenticated and all checks pass`() = runBlocking {
            val authData =
                AuthData("username", WordpressToken("token", "user_email", "user_nicename", "user_display_name"))
            coEvery { datastorePref.getAuthData() } returns authData
            coEvery { firebaseService.isAuthenticated() } returns true
            coEvery { wordpressRepo.validateToken(authData.token) } returns AuthResult.Success(Unit)

            authManager.restoreAuthStateFromStorage()
            val result = authManager.isAuthValid()

            assertTrue(result)
        }

        @Test
        fun `isAuthValid returns false when authenticated but Firebase authentication fails`() = runBlocking {
            val authData =
                AuthData("username", WordpressToken("token", "user_email", "user_nicename", "user_display_name"))
            coEvery { datastorePref.getAuthData() } returns authData
            coEvery { firebaseService.isAuthenticated() } returns false

            val result = authManager.isAuthValid()

            assertFalse(result)
        }

        @Test
        fun `isAuthValid returns false when authenticated but WordPress token validation fails`() = runBlocking {
            val authData =
                AuthData("username", WordpressToken("token", "user_email", "user_nicename", "user_display_name"))
            coEvery { datastorePref.getAuthData() } returns authData
            coEvery { firebaseService.isAuthenticated() } returns true
            coEvery { wordpressRepo.validateToken(authData.token) } returns AuthResult.Unauthorized

            val result = authManager.isAuthValid()

            assertFalse(result)
        }
    }

    class LoginTests : BaseTest() {

        @Test
        fun `login with valid credentials sets Authenticated`() = runBlocking {
            val username = "username"
            val password = "password"
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")

            coEvery { wordpressRepo.authenticate(AuthentificationBody(username, password)) } returns AuthResult.Success(
                token
            )
            coEvery { firebaseService.authenticate() } just runs
            coEvery { wordpressRepo.getUserId(username) } returns "userId"
            coEvery { datastorePref.setAuthData(any()) } just runs
            coEvery { datastorePref.setIsConnectedLeastOneTime(true) } just runs
            coEvery { datastorePref.setWasConnectedLastTime(true) } just runs
            coEvery { datastorePref.setUserId("userId") } just runs

            val result = authManager.login(username, password)

            assertTrue(result is AuthResult.Success)
            assertTrue(authManager.authState.value is AuthState.Authenticated)

            coVerify { datastorePref.setAuthData(any()) }
        }

        @Test
        fun `login with invalid credentials returns Unauthorized`() = runBlocking {
            val username = "username"
            val password = "password"

            coEvery {
                wordpressRepo.authenticate(
                    AuthentificationBody(
                        username,
                        password
                    )
                )
            } returns AuthResult.Unauthorized
            coEvery { firebaseService.authenticate() } just runs

            val result = authManager.login(username, password)

            assertTrue(result is AuthResult.Unauthorized)
            assertTrue(authManager.authState.value is AuthState.Unauthenticated)
        }

        @Test
        fun `login with network error on wordpressRepository returns NetworkError`() = runBlocking {
            val username = "username"
            val password = "password"

            coEvery {
                wordpressRepo.authenticate(
                    AuthentificationBody(
                        username,
                        password
                    )
                )
            } returns AuthResult.NetworkError
            coEvery { firebaseService.authenticate() } just runs

            val result = authManager.login(username, password)

            assertTrue(result is AuthResult.NetworkError)
            assertTrue(authManager.authState.value is AuthState.Unauthenticated)
        }

        @Test
        fun `login with network error on firebaseService returns NetworkError`() = runBlocking {
            val username = "username"
            val password = "password"
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")

            coEvery {
                wordpressRepo.authenticate(
                    AuthentificationBody(
                        username,
                        password
                    )
                )
            } returns AuthResult.Success(token)
            coEvery { firebaseService.authenticate() } throws UnknownHostException()
            coEvery { wordpressRepo.isNetworkError(any()) } returns true

            val result = authManager.login(username, password)

            assertTrue(result is AuthResult.NetworkError)
            assertTrue(authManager.authState.value is AuthState.Unauthenticated)
        }
    }

    class LogoutTests : BaseTest() {
        @Test
        fun `logout sets AuthState to Unauthenticated`() = runBlocking {
            coEvery { firebaseService.signOut() } just runs
            coEvery { datastorePref.clearAuthData() } just runs
            coEvery { datastorePref.setWasConnectedLastTime(false) } just runs

            authManager.logout()

            assertTrue(authManager.authState.value is AuthState.Unauthenticated)
        }

        @Test
        fun `logout calls firebaseService signOut`() = runBlocking {
            coEvery { firebaseService.signOut() } just runs
            coEvery { datastorePref.clearAuthData() } just runs
            coEvery { datastorePref.setWasConnectedLastTime(false) } just runs

            authManager.logout()

            coVerify { firebaseService.signOut() }
        }

        @Test
        fun `logout calls datastorePref clearAuthData`() = runBlocking {
            coEvery { firebaseService.signOut() } just runs
            coEvery { datastorePref.clearAuthData() } just runs
            coEvery { datastorePref.setWasConnectedLastTime(false) } just runs

            authManager.logout()

            coVerify { datastorePref.clearAuthData() }
        }

        @Test
        fun `logout calls datastorePref setWasConnectedLastTime false`() = runBlocking {
            coEvery { firebaseService.signOut() } just runs
            coEvery { datastorePref.clearAuthData() } just runs
            coEvery { datastorePref.setWasConnectedLastTime(false) } just runs

            authManager.logout()

            coVerify { datastorePref.setWasConnectedLastTime(false) }
        }
    }

}