package com.xpeho.xpeapp.data.model.qvst

data class QvstCampaign(
    val id: String,
    val name: String,
    val theme: QvstTheme,
    val status: String,
    val start_date: String,
    val end_date: String,
    val participation_rate: String
)
