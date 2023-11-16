package com.xpeho.xpeapp.data

const val NEWSLETTERS_COLLECTION = "newsletters"
const val FEATURE_FLIPPING_COLLECTION = "featureFlipping"
const val EXPENSE_REPORT_COLLECTION = "expenseReport"

enum class FeatureFlippingEnum(val value: String) {
    NEWSLETTERS("newsletters"),
    VACATION("vacation"),
    CRA("cra"),
    EXPENSE_REPORT("expenseReport"),
    COLLEAGUES("colleagues"),
}

const val ALPHA_BOX = 0.5f