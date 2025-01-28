package com.xpeho.xpeapp.data

import android.util.Log
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.entity.QvstAnswerBody
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.entity.user.UserEditPassword
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.model.qvst.QvstAnswer
import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstProgress
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion
import com.xpeho.xpeapp.data.model.qvst.QvstTheme
import com.xpeho.xpeapp.data.model.user.UpdatePasswordResult
import com.xpeho.xpeapp.data.model.user.UserInfos
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.data.service.WordpressService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.net.ssl.SSLHandshakeException

@RunWith(Enclosed::class)
class WordpressRepositoryTest {
    abstract class BaseTest {
        protected lateinit var wordpressRepo: WordpressRepository
        protected lateinit var wordpressService: WordpressService

        @Before
        fun setUp() {
            wordpressService = mockk()
            wordpressRepo = spyk(WordpressRepository(wordpressService))
            mockkStatic(Log::class)
            every { Log.e(any(), any()) } returns 0
            every { Log.d(any(), any()) } returns 0
        }
    }

    class AuthenticateTests : BaseTest() {

        @Test
        fun `authenticate with valid credentials returns Success`() = runBlocking {
            val credentials = AuthentificationBody("username", "password")
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            coEvery { wordpressService.authentification(credentials) } returns token

            val result = wordpressRepo.authenticate(credentials)

            assert(result is AuthResult.Success)
            assertEquals(token, (result as AuthResult.Success).data)
        }

        @Test
        fun `authenticate with error calls handleAuthExceptions`() = runBlocking {
            val credentials = AuthentificationBody("username", "password")
            coEvery { wordpressService.authentification(credentials) } throws HttpException(mockk {
                coEvery { code() } returns 403
                coEvery { message() } returns "Forbidden"
            })

            wordpressRepo.authenticate(credentials)

            verify { wordpressRepo.handleAuthExceptions(any()) }
        }
    }

    class ValidateTokenTests : BaseTest() {

        @Test
        fun `validateToken with valid token returns Success`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            coEvery { wordpressService.validateToken(any()) } returns Unit

            val result = wordpressRepo.validateToken(token)

            assertTrue(result is AuthResult.Success)
        }

        @Test
        fun `validateToken with error calls handleAuthException`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            coEvery { wordpressService.validateToken(any()) } throws HttpException(mockk {
                coEvery { code() } returns 403
                coEvery { message() } returns "Forbidden"
            })

            wordpressRepo.validateToken(token)

            coVerify { wordpressRepo.handleAuthExceptions(any()) }
        }
    }

    class GetUserIdTests : BaseTest() {

        @Test
        fun `getUserId with valid username returns userId`() = runBlocking {
            val username = "username"
            coEvery { wordpressService.getUserId(username) } returns "userId"

            val result = wordpressRepo.getUserId(username)

            assertEquals("userId", result)
        }

        @Test
        fun `getUserId with error returns null`() = runBlocking {
            val username = "username"
            coEvery { wordpressService.getUserId(username) } throws UnknownHostException()

            val result = wordpressRepo.getUserId(username)

            assertEquals(null, result)
        }
    }

    class ClassifyCampaignsTests : BaseTest() {
        @Test
        fun `classifyCampaigns with valid end date returns date year key`() {
            val campaigns = arrayListOf(
                QvstCampaignEntity(
                    id = "2",
                    name = "Campaign 2",
                    themeName = "Theme 2",
                    status = "CLOSED",
                    outdated = true,
                    completed = true,
                    remainingDays = 0,
                    endDate = "2022-12-31",
                    resultLink = "resultLink"
                )
            )

            val result = wordpressRepo.classifyCampaigns(campaigns)

            assertEquals(1, result[2022]?.size)
            assertEquals("Campaign 2", result[2022]?.get(0)?.name)
        }

        @Test(expected = DateTimeParseException::class)
        fun `classifyCampaigns with invalid end date returns empty`() {
            val campaigns = arrayListOf(
                QvstCampaignEntity(
                    id = "3",
                    name = "Campaign 3",
                    themeName = "Theme 3",
                    status = "CLOSED",
                    outdated = true,
                    completed = true,
                    remainingDays = 0,
                    endDate = "invalid-date",
                    resultLink = "resultLink"
                )
            )

            val result = wordpressRepo.classifyCampaigns(campaigns)

            assertEquals(0, result.size)
        }
    }

    class GetCampaignsEntitiesFromModelsTests : BaseTest() {

        @Test
        fun `getCampaignsEntitiesFromModels with remaining days greater than 0`() {
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName",
                    ),
                    status = "OPEN",
                    startDate = LocalDate.now().minusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    endDate = LocalDate.now().plusDays(10).toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf<QvstProgress>()

            val result = wordpressRepo.getCampaignsEntitiesFromModels(campaigns, progress)

            assertEquals(1, result.size)
            assertEquals("campaignName", result[0].name)
            assertEquals("themeName", result[0].themeName)
            assertEquals(10, result[0].remainingDays)
            assertEquals(false, result[0].outdated)
            assertEquals("resultLink", result[0].resultLink)
        }

        @Test
        fun `getCampaignsEntitiesFromModels with remaining days less than or equal to 0`() {
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName",
                    ),
                    status = "CLOSED",
                    startDate = LocalDate.now().minusDays(10).toString(),
                    endDate = LocalDate.now().minusDays(1).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf<QvstProgress>()

            val result = wordpressRepo.getCampaignsEntitiesFromModels(campaigns, progress)

            assertEquals(1, result.size)
            assertEquals("campaignName", result[0].name)
            assertEquals("themeName", result[0].themeName)
            assertEquals(0, result[0].remainingDays)
            assertEquals(true, result[0].outdated)
            assertEquals("resultLink", result[0].resultLink)
        }

        @Test
        fun `getCampaignsEntitiesFromModels with completed progress`() {
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName"
                    ),
                    status = "OPEN",
                    endDate = LocalDate.now().plusDays(5).toString(),
                    startDate = LocalDate.now().minusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf(
                QvstProgress(
                    campaignId = "campaignId",
                    answeredQuestions = 10,
                    totalQuestions = 10,
                    userId = "userId"
                )
            )

            val result = wordpressRepo.getCampaignsEntitiesFromModels(campaigns, progress)

            assertEquals(1, result.size)
            assertEquals("campaignName", result[0].name)
            assertEquals("themeName", result[0].themeName)
            assertEquals(true, result[0].completed)
            assertEquals("resultLink", result[0].resultLink)
        }

        @Test
        fun `getCampaignsEntitiesFromModels without progress`() {
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName"
                    ),
                    status = "OPEN",
                    endDate = LocalDate.now().plusDays(5).toString(),
                    startDate = LocalDate.now().minusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf<QvstProgress>()

            val result = wordpressRepo.getCampaignsEntitiesFromModels(campaigns, progress)

            assertEquals(1, result.size)
            assertEquals("campaignName", result[0].name)
            assertEquals("themeName", result[0].themeName)
            assertEquals(false, result[0].completed)
            assertEquals("resultLink", result[0].resultLink)
        }

        @Test
        fun `getCampaignsEntitiesFromModels filter draft campaigns`() {
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName"
                    ),
                    status = "DRAFT",
                    endDate = LocalDate.now().plusDays(5).toString(),
                    startDate = LocalDate.now().minusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf(
                QvstProgress(
                    campaignId = "campaignId",
                    answeredQuestions = 10,
                    totalQuestions = 10,
                    userId = "userId"
                )
            )

            val result = wordpressRepo.getCampaignsEntitiesFromModels(campaigns, progress)

            assertEquals(0, result.size)
        }
    }

    class GetQvstCampaignsTests : BaseTest() {

        @Test
        fun `getQvstCampaigns with valid token and username returns campaigns`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            val username = "username"
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName"
                    ),
                    status = "OPEN",
                    startDate = LocalDate.now().minusDays(10).toString(),
                    endDate = LocalDate.now().plusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf<QvstProgress>()

            coEvery { wordpressService.getQvstCampaigns() } returns campaigns
            coEvery { wordpressService.getQvstProgressByUserId(any()) } returns progress
            coEvery { wordpressService.getUserId(username) } returns "userId"

            val result = wordpressRepo.getQvstCampaigns(username)

            assertEquals(1, result?.size)
            assertEquals("campaignName", result?.get(0)?.name)
        }

        @Test
        fun `getQvstCampaigns with network error returns null`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            val username = "username"

            coEvery { wordpressService.getQvstCampaigns() } throws UnknownHostException()

            val result = wordpressRepo.getQvstCampaigns(username)

            assertEquals(null, result)
        }

        @Test
        fun `getQvstCampaigns with onlyActive with valid token and username returns campaigns`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            val username = "username"
            val campaigns = listOf(
                QvstCampaign(
                    id = "campaignId",
                    name = "campaignName",
                    theme = QvstTheme(
                        "themeId",
                        name = "themeName"
                    ),
                    status = "OPEN",
                    startDate = LocalDate.now().minusDays(10).toString(),
                    endDate = LocalDate.now().plusDays(10).toString(),
                    participationRate = 0.5.toString(),
                    action = "resultLink"
                )
            )
            val progress = listOf<QvstProgress>()

            coEvery { wordpressService.getQvstCampaigns(":active") } returns campaigns
            coEvery { wordpressService.getQvstProgressByUserId(any()) } returns progress
            coEvery { wordpressService.getUserId(username) } returns "userId"

            val result = wordpressRepo.getQvstCampaigns(username, true)

            assertEquals(1, result?.size)
            assertEquals("campaignName", result?.get(0)?.name)
        }

        @Test
        fun `getQvstCampaigns with onlyActive with network error returns null`() = runBlocking {
            val token = WordpressToken("token", "user_email", "user_nicename", "user_display_name")
            val username = "username"

            coEvery { wordpressService.getQvstCampaigns(":active") } throws UnknownHostException()

            val result = wordpressRepo.getQvstCampaigns(username, true)

            assertEquals(null, result)
        }
    }

    class GetQvstQuestionsByCampaignId : BaseTest() {

        @Test
        fun `getQvstQuestionsByCampaignId with valid campaignId and userId returns questions`() = runBlocking {
            val campaignId = "campaignId"
            val userId = "userId"
            val questions = listOf(
                QvstQuestion(
                    questionId = "questionId",
                    question = "question",
                    hasAnswered = false,
                    answers = listOf(QvstAnswer("answerId", "answer", "value")),
                    userAnswer = null
                )
            )

            coEvery { wordpressService.getQvstQuestionsByCampaignId(campaignId, userId) } returns questions

            val result = wordpressRepo.getQvstQuestionsByCampaignId(campaignId, userId)

            assertEquals(1, result?.size)
            assertEquals("question", result?.get(0)?.question)
        }

        @Test
        fun `getQvstQuestionsByCampaignId with network error returns null`() = runBlocking {
            val campaignId = "campaignId"
            val userId = "userId"

            coEvery { wordpressService.getQvstQuestionsByCampaignId(campaignId, userId) } throws UnknownHostException()

            val result = wordpressRepo.getQvstQuestionsByCampaignId(campaignId, userId)

            assertEquals(null, result)
        }
    }

    class SubmitAnswersTest : BaseTest() {

        @Test
        fun `submitAnswers with valid campaignId, userId and answers returns true`() = runBlocking {
            val campaignId = "campaignId"
            val userId = "userId"
            val answers = listOf(
                QvstAnswerBody("questionId", "answerId")
            )

            coEvery { wordpressService.submitAnswers(campaignId, userId, answers) } returns true

            val result = wordpressRepo.submitAnswers(campaignId, userId, answers)

            assertTrue(result)
        }

        @Test
        fun `submitAnswers with network error returns false`() = runBlocking {
            val campaignId = "campaignId"
            val userId = "userId"
            val answers = listOf(
                QvstAnswerBody("questionId", "answerId")
            )

            coEvery { wordpressService.submitAnswers(campaignId, userId, answers) } throws UnknownHostException()

            val result = wordpressRepo.submitAnswers(campaignId, userId, answers)

            assertFalse(result)
        }
    }

    class FetchUserInfosTests : BaseTest() {

        @Test
        fun `fetchUserInfos with valid response returns UserInfos`() = runBlocking {
            val userInfos = UserInfos("1", "toto@example", "toto","tata")
            coEvery { wordpressService.fetchUserInfos() } returns userInfos

            val result = wordpressRepo.fetchUserInfos()

            assertEquals(userInfos, result)
        }

        @Test
        fun `fetchUserInfos with network error returns null`() = runBlocking {
            coEvery { wordpressService.fetchUserInfos() } throws UnknownHostException()

            val result = wordpressRepo.fetchUserInfos()

            assertEquals(null, result)
        }

        @Test
        fun `fetchUserInfos with HttpException returns null`() = runBlocking {
            coEvery { wordpressService.fetchUserInfos() } throws HttpException(mockk {
                coEvery { code() } returns 500
                coEvery { message() } returns "Internal Server Error"
            })

            val result = wordpressRepo.fetchUserInfos()

            assertEquals(null, result)
        }
    }

    class UpdatePasswordTests : BaseTest() {
        companion object {
            private const val INTERNAL_SERVER_ERROR = 500
            private const val NO_CONTENT = 204
        }

        @Test
        fun `updatePassword with valid response returns Success`() = runBlocking {
            val editPassword = UserEditPassword("oldPassword", "newPassword", "newPassword")
            val response = mockk<Response<String>> {
                every { code() } returns NO_CONTENT
            }
            coEvery { wordpressService.updatePassword(editPassword) } returns response

            val result = wordpressRepo.updatePassword(editPassword)

            assertEquals(UpdatePasswordResult.Success, result)
        }

        @Test
        fun `updatePassword with incorrect initial password returns IncorrectInitialPassword`() = runBlocking {
            val editPassword = UserEditPassword("oldPassword", "newPassword", "newPassword")
            val response = mockk<Response<String>> {
                every { code() } returns INTERNAL_SERVER_ERROR
                every { errorBody()?.string() } returns "incorrect_password"
            }
            coEvery { wordpressService.updatePassword(editPassword) } returns response

            val result = wordpressRepo.updatePassword(editPassword)

            assertEquals(UpdatePasswordResult.IncorrectInitialPassword, result)
        }

        @Test
        fun `updatePassword with password mismatch returns PasswordMismatch`() = runBlocking {
            val editPassword = UserEditPassword("oldPassword", "newPassword", "newPassword")
            val response = mockk<Response<String>> {
                every { code() } returns INTERNAL_SERVER_ERROR
                every { errorBody()?.string() } returns "password_mismatch"
            }
            coEvery { wordpressService.updatePassword(editPassword) } returns response

            val result = wordpressRepo.updatePassword(editPassword)

            assertEquals(UpdatePasswordResult.PasswordMismatch, result)
        }

        @Test
        fun `updatePassword with unknown error returns NetworkError`() = runBlocking {
            val editPassword = UserEditPassword("oldPassword", "newPassword", "newPassword")
            val response = mockk<Response<String>> {
                every { code() } returns INTERNAL_SERVER_ERROR
                every { errorBody() } returns null
            }
            coEvery { wordpressService.updatePassword(editPassword) } returns response

            val result = wordpressRepo.updatePassword(editPassword)

            assertEquals(UpdatePasswordResult.NetworkError, result)
        }

        @Test
        fun `updatePassword with network error returns NetworkError`() = runBlocking {
            val editPassword = UserEditPassword("oldPassword", "newPassword", "newPassword")
            coEvery { wordpressService.updatePassword(editPassword) } throws UnknownHostException()

            val result = wordpressRepo.updatePassword(editPassword)

            assertEquals(UpdatePasswordResult.NetworkError, result)
        }
    }


    class HandleAuthExceptionsTests : BaseTest() {

        @Test
        fun `handleAuthExceptions with network error returns NetworkError`() = runBlocking {
            every { wordpressRepo.isNetworkError(any()) } returns true

            val result = wordpressRepo.handleAuthExceptions(Exception())

            assertEquals(AuthResult.NetworkError, result)
        }

        @Test
        fun `handleAuthExceptions with 403 error returns Unauthorized`() = runBlocking {
            val result = wordpressRepo.handleAuthExceptions(
                HttpException(
                    mockk {
                        coEvery { code() } returns 403
                        coEvery { message() } returns "message"
                    }
                )
            )
            assertEquals(AuthResult.Unauthorized, result)
        }

        @Test
        fun `handleAuthExceptions with unknown error throws unknown exception`() = runBlocking {
            val exception: java.lang.Exception = assertThrows(RuntimeException::class.java) {
                runBlocking {
                    wordpressRepo.handleAuthExceptions(
                        RuntimeException("kaboom")
                    )
                }
            }

            assertEquals("kaboom", exception.message)
        }
    }

    class HandleServiceExceptionsTests : BaseTest() {

        @Test
        fun `handleServiceExceptions with network error calls catchBody`() = runBlocking {
            val tryBody = mockk<() -> Unit>()
            val catchBody = mockk<(Exception) -> Unit>()
            val exceptionInstance = UnknownHostException()

            every { tryBody() } throws exceptionInstance
            every { catchBody(any()) } just runs
            every { wordpressRepo.isNetworkError(exceptionInstance) } returns true

            wordpressRepo.handleServiceExceptions(tryBody, catchBody)

            verify { catchBody(exceptionInstance) }
        }

        @Test
        fun `handleServiceExceptions with unknown exception throws unknown exception`() = runBlocking {
            val tryBody = mockk<() -> Unit>()
            val catchBody = mockk<(Exception) -> Unit>()
            val exceptionInstance = RuntimeException("Unknown error")

            every { tryBody() } throws exceptionInstance

            val exception: java.lang.Exception = assertThrows(RuntimeException::class.java) {
                wordpressRepo.handleServiceExceptions(tryBody, catchBody)
            }

            assertEquals("Unknown error", exception.message)
        }
    }

    class IsNetworkErrorTests : BaseTest() {

        @Test
        fun `isNetworkError with UnknownHostException returns True`() {
            val exception = UnknownHostException()
            val result = wordpressRepo.isNetworkError(exception)
            assertTrue(result)
        }

        @Test
        fun `isNetworkError with SocketTimeoutException returns True`() {
            val exception = SocketTimeoutException()
            val result = wordpressRepo.isNetworkError(exception)
            assertTrue(result)
        }

        @Test
        fun `isNetworkError with SSLHandshakeException returns True`() {
            val exception = SSLHandshakeException("SSL error")
            val result = wordpressRepo.isNetworkError(exception)
            assertTrue(result)
        }

        @Test
        fun `isNetworkError with ConnectException returns True`() {
            val exception = ConnectException()
            val result = wordpressRepo.isNetworkError(exception)
            assertTrue(result)
        }

        @Test
        fun `isNetworkError with HttpException returns True`() {
            val exception = HttpException(mockk {
                coEvery { code() } returns 503
                coEvery { message() } returns "Service unavailable"
            })
            val result = wordpressRepo.isNetworkError(exception)
            assertTrue(result)
        }

        @Test
        fun `isNetworkError with unknown exception returns False`() {
            val exception = RuntimeException("Other error")
            val result = wordpressRepo.isNetworkError(exception)
            assertFalse(result)
        }
    }

    class CountDaysBetweenTests : BaseTest() {

        @Test
        fun `countDaysBetween with valid dates returns correct days count`() {
            val startDate = "2023-01-01"
            val endDate = "2023-01-10"
            val result = wordpressRepo.countDaysBetween(startDate, endDate)
            assertEquals(9, result)
        }

        @Test
        fun `countDaysBetween with same dates returns zero`() {
            val startDate = "2023-01-01"
            val endDate = "2023-01-01"
            val result = wordpressRepo.countDaysBetween(startDate, endDate)
            assertEquals(0, result)
        }

        @Test
        fun `countDaysBetween with end date before start date returns negative days count`() {
            val startDate = "2023-01-10"
            val endDate = "2023-01-01"
            val result = wordpressRepo.countDaysBetween(startDate, endDate)
            assertEquals(-9, result)
        }

        @Test(expected = DateTimeParseException::class)
        fun `countDaysBetween with invalid start date throws DateTimeParseException`() {
            val startDate = "invalid-date"
            val endDate = "2023-01-10"
            wordpressRepo.countDaysBetween(startDate, endDate)
        }

        @Test(expected = DateTimeParseException::class)
        fun `countDaysBetween with invalid end date throws DateTimeParseException`() {
            val startDate = "2023-01-01"
            val endDate = "invalid-date"
            wordpressRepo.countDaysBetween(startDate, endDate)
        }

        @Test
        fun `countDaysBetween with different pattern returns correctDays`() {
            val startDate = "01-01-2023"
            val endDate = "10-01-2023"
            val pattern = "dd-MM-yyyy"
            val result = wordpressRepo.countDaysBetween(startDate, endDate, pattern)
            assertEquals(9, result)
        }
    }

    class GetTodayDateStringTests : BaseTest() {

        @Test
        fun `getTodayDateString with default pattern returns correctDate`() {
            val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val result = wordpressRepo.getTodayDateString()
            assertEquals(expectedDate, result)
        }

        @Test
        fun `getTodayDateString with custom pattern returns correctDate`() {
            val pattern = "dd-MM-yyyy"
            val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
            val result = wordpressRepo.getTodayDateString(pattern)
            assertEquals(expectedDate, result)
        }
    }
}