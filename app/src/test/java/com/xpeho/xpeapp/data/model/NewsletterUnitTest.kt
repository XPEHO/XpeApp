package com.xpeho.xpeapp.data.model

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class NewsletterUnitTest {

    @Test
    fun testEmptyNewsletter() {

        val dateTesting = LocalDate.now()

        val emptyNewsletter = Newsletter(
            id = "",
            summary = "",
            date = dateTesting,
            publicationDate = dateTesting,
            pdfUrl = "",
        )

        assertEquals("", emptyNewsletter.id)
        assertEquals("", emptyNewsletter.summary)
        assertEquals(dateTesting, emptyNewsletter.date)
        assertEquals(dateTesting, emptyNewsletter.publicationDate)
        assertEquals("", emptyNewsletter.pdfUrl)
    }
}