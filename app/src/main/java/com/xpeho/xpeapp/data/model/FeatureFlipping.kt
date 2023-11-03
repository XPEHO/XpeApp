package com.xpeho.xpeapp.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class FeatureFlipping(
    val id: String,
    val name: String,
    val description : String,
    val enabled: Boolean,
)

fun emptyFeatureFlipping() = FeatureFlipping(
    id = "",
    name = "",
    description = "",
    enabled = false,
)

fun DocumentSnapshot?.toFeatureFlipping(): FeatureFlipping {
    return FeatureFlipping(
        id = this?.id ?: "",
        name = this?.getString("name") ?: "",
        description = this?.getString("description") ?: "",
        enabled = this?.getBoolean("enabled") ?: false
    )
}