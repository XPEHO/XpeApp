package com.xpeho.xpeapp.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class FeatureFlipping(
    val id: String,
    val name: String,
    val description: String,
    val uatEnabled: Boolean,
    val prodEnabled: Boolean,
)

fun DocumentSnapshot?.toFeatureFlipping(): FeatureFlipping {
    return FeatureFlipping(
        id = this?.id ?: "",
        name = this?.getString("name") ?: "",
        description = this?.getString("description") ?: "",
        uatEnabled = this?.getBoolean("uatEnabled") ?: false,
        prodEnabled = this?.getBoolean("prodEnabled") ?: false,
    )
}
