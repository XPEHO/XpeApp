package com.xpeho.xpeapp.data.model.qvst

import com.google.gson.annotations.SerializedName

data class QvstQuestion(
    @SerializedName("question_id") val questionId: String,
    val question: String,
    val hasAnswered: Boolean,
    val answers: List<QvstAnswer>,
    var userAnswer: String? = null,
)