package com.xpeho.xpeapp.domain

import android.util.Log
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.service.FirebaseService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(Enclosed::class)
class FeatureFlippingManagerTest {

    abstract class BaseTest {
        protected lateinit var featureFlippingManager: FeatureFlippingManager
        protected lateinit var firebaseService: FirebaseService

        @Before
        fun setUp() {
            firebaseService = mockk()

            // Mock android.util.Log methods
            mockkStatic(Log::class)
            every { Log.e(any(), any()) } returns 0

            featureFlippingManager = FeatureFlippingManager(firebaseService)
        }
    }

    class GetStateTests : BaseTest() {
        @Test
        fun `getState returns initial loading state`() = runBlocking {
            val state = featureFlippingManager.getState()
            assertTrue(state.value is FeatureFlippingState.LOADING)
        }
    }

    class IsFeatureEnabledTests : BaseTest() {
        @Test
        fun `isFeatureEnabled returns false when state is loading`() = runBlocking {
            val result = featureFlippingManager.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)
            assertFalse(result)
        }

        @Test
        fun `isFeatureEnabled returns true when feature is enabled`() = runBlocking {
            val featureEnabled = mapOf(FeatureFlippingEnum.NEWSLETTERS to true)
            featureFlippingManager.getState().value = FeatureFlippingState.SUCCESS(featureEnabled)

            val result = featureFlippingManager.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)
            assertTrue(result)
        }

        @Test
        fun `isFeatureEnabled returns false when feature is not enabled`() = runBlocking {
            val featureEnabled = mapOf(FeatureFlippingEnum.NEWSLETTERS to false)
            featureFlippingManager.getState().value = FeatureFlippingState.SUCCESS(featureEnabled)

            val result = featureFlippingManager.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)
            assertFalse(result)
        }
    }

    class UpdateTests : BaseTest() {
        @Test
        fun `update fetches data`() = runBlocking {
            coEvery { firebaseService.fetchFeatureFlipping() } returns emptyList()

            featureFlippingManager.update()

            // Wait for the update to complete
            while (featureFlippingManager.getState().value is FeatureFlippingState.LOADING) {
                // Yield to other coroutines
                kotlinx.coroutines.delay(10)
            }

            coVerify { firebaseService.fetchFeatureFlipping() }
        }
    }

    class FetchDataTests : BaseTest() {
        @Test
        fun `fetchData sets error state on network error`() = runBlocking {
            coEvery { firebaseService.fetchFeatureFlipping() } throws IOException("Network error")

            featureFlippingManager.update()

            // Wait for the update to complete
            while (featureFlippingManager.getState().value is FeatureFlippingState.LOADING) {
                // Yield to other coroutines
                kotlinx.coroutines.delay(10)
            }

            assertTrue(featureFlippingManager.getState().value is FeatureFlippingState.ERROR)
        }

        @Test
        fun `fetchData sets success state on valid data`() = runBlocking {
            val featureFlipping = FeatureFlipping("newsletters", "Newsletter", "Description", true, false)
            coEvery { firebaseService.fetchFeatureFlipping() } returns listOf(featureFlipping)

            featureFlippingManager.update()

            // Wait for the update to complete
            while (featureFlippingManager.getState().value is FeatureFlippingState.LOADING) {
                // Yield to other coroutines
                kotlinx.coroutines.delay(10)
            }

            assertTrue(featureFlippingManager.getState().value is FeatureFlippingState.SUCCESS)
        }

        @Test
        fun `fetchData sets error state when feature is not found in enum`() = runBlocking {
            val featureFlipping = FeatureFlipping(
                "invalid_feature_id",
                "invalid_feature_name",
                "invalid_feature_description",
                true,
                false
            )
            coEvery { firebaseService.fetchFeatureFlipping() } returns listOf(featureFlipping)

            featureFlippingManager.update()

            // Wait for the update to complete
            while (featureFlippingManager.getState().value is FeatureFlippingState.LOADING) {
                // Yield to other coroutines
                kotlinx.coroutines.delay(10)
            }

            assertTrue(featureFlippingManager.getState().value is FeatureFlippingState.ERROR)
        }
    }
}