package com.xpeho.xpeapp.data

const val NEWSLETTERS_COLLECTION = "newsletters"
const val FEATURE_FLIPPING_COLLECTION = "featureFlipping"

enum class FeatureFlippingEnum(val value: String) {
    NEWSLETTERS("newsletters"),
    VACATION("vacation"),
    CRA("cra"),
    EXPENSE_REPORT("expenseReport"),
    COLLEAGUES("colleagues"),
    QVST("campaign"),
}
