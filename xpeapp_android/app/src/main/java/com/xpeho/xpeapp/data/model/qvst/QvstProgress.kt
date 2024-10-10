package com.xpeho.xpeapp.data.model.qvst

data class QvstProgress (
    val user_id: String,
    val campaign_id: String,
    val answered_questions: Int,
    val total_questions: Int,
)