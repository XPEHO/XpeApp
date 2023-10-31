package com.xpeho.xpeapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate

data class Newsletter(
    val id: String,
    val summary: String,
    @ServerTimestamp
    val date: LocalDate,
    @ServerTimestamp
    val publicationDate: LocalDate,
    val pdfUrl: String,
)
