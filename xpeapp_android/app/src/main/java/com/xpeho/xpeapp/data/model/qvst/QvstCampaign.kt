package com.xpeho.xpeapp.data.model.qvst

import com.google.gson.annotations.SerializedName

data class QvstCampaign(
    val id: String,
    val name: String,
    val theme: QvstTheme,
    val status: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("participation_rate") val participationRate: String
)