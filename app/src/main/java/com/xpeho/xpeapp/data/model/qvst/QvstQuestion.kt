package com.xpeho.xpeapp.data.model.qvst

data class QvstQuestion(
    val question_id: String,
    val question: String,
    val theme: String,
    val theme_id: String,
    val answers: List<QvstAnswer>
)
