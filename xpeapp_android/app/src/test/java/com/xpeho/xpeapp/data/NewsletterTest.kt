package com.xpeho.xpeapp.data

import com.xpeho.xpeapp.data.model.Newsletter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class NewsletterTest {

    @Test
    fun testNewsletterInitialization() {
        val id = "1"
        val summary = "This is a summary"
        val picture = "path/to/image.jpg"
        val date = LocalDate.of(2023, 10, 1)
        val publicationDate = LocalDate.of(2023, 10, 2)
        val pdfUrl = "http://example.com/newsletter.pdf"

        val newsletter = Newsletter(id, summary, picture, date, publicationDate, pdfUrl)

        assertEquals(id, newsletter.id)
        assertEquals(summary, newsletter.summary)
        assertEquals(picture, newsletter.picture)
        assertEquals(date, newsletter.date)
        assertEquals(publicationDate, newsletter.publicationDate)
        assertEquals(pdfUrl, newsletter.pdfUrl)
    }

    @Test
    fun testNewsletterInitializationWithNullPicture() {
        val id = "2"
        val summary = "This is another summary"
        val date = LocalDate.of(2023, 10, 3)
        val publicationDate = LocalDate.of(2023, 10, 4)
        val pdfUrl = "http://example.com/another_newsletter.pdf"

        val newsletter = Newsletter(id, summary, null, date, publicationDate, pdfUrl)

        assertEquals(id, newsletter.id)
        assertEquals(summary, newsletter.summary)
        assertNull(newsletter.picture)
        assertEquals(date, newsletter.date)
        assertEquals(publicationDate, newsletter.publicationDate)
        assertEquals(pdfUrl, newsletter.pdfUrl)
    }
}