package com.xpeho.xpeapp.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TokenProviderTest {
    @Test
    fun testGetToken() {
        val tokenProvider = TokenProvider()
        assertNull(tokenProvider.get())

        val token = "testToken"
        tokenProvider.set(token)
        assertEquals(token, tokenProvider.get())
    }

    @Test
    fun testSetToken() {
        val tokenProvider = TokenProvider()
        val token = "newToken"
        tokenProvider.set(token)
        assertEquals(token, tokenProvider.get())
    }
}