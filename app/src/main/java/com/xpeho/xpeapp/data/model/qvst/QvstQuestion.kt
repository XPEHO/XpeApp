package com.xpeho.xpeapp.data.model.qvst

data class QvstQuestion(
        val question_id: String,
        val question: String,
        val hasAnswered: Boolean,
        val answers: List<QvstAnswer>,
        var userAnswer: String? = null,
)
