package com.xpeho.xpeapp.data.model.qvst

import com.google.gson.annotations.SerializedName

data class QvstProgress(
    @SerializedName("user_id") val userId: String,
    @SerializedName("campaign_id") val campaignId: String,
    @SerializedName("answered_questions") val answeredQuestions: Int,
    @SerializedName("total_questions") val totalQuestions: Int,
)