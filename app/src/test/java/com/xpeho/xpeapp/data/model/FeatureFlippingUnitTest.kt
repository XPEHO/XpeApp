package com.xpeho.xpeapp.data.model

import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureFlippingTest {

    private val mockSnapshot = mockk<DocumentSnapshot>()

    @Test
    fun testEmptyFeatureFlipping() {
        val emptyFeature = emptyFeatureFlipping()

        assertEquals("", emptyFeature.id)
        assertEquals("", emptyFeature.name)
        assertEquals("", emptyFeature.description)
        assertEquals(false, emptyFeature.enabled)
    }

    @Test
    fun testFeatureFlipping() {
        val feature = mockSnapshot().toFeatureFlipping()

        assertEquals("id", feature.id)
        assertEquals("name", feature.name)
        assertEquals("description", feature.description)
        assertEquals(true, feature.enabled)
    }

    private fun mockSnapshot(): DocumentSnapshot {
        return mockSnapshot.apply {
            every { id } returns "id"
            every { getString("name") } returns "name"
            every { getString("description") } returns "description"
            every { getBoolean("enabled") } returns true
        }
    }
}
