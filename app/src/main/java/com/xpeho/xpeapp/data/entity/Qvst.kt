package com.xpeho.xpeapp.data.entity

import com.xpeho.xpeapp.data.model.qvst.QvstCampaign
import com.xpeho.xpeapp.data.model.qvst.QvstQuestion

data class Qvst(
    val id: Int,
    val campaign: QvstCampaign,
    val questions: List<QvstQuestion> = emptyList()
)
