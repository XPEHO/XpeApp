package com.xpeho.xpeapp.data.entity

data class QvstCampaignEntity(
    val id: String,
    val name: String,
    val themeName: String,
    val status: String,
    val outdated: Boolean,
    val completed: Boolean,
    val remainingDays: Int,
    val endDate: String,
    val resultLink: String,
)