package com.xpeho.xpeapp.data.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseService {
    suspend fun authenticate() {
        FirebaseAuth.getInstance().signInAnonymously().await()
    }

    fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null

    fun signOut() = FirebaseAuth.getInstance().signOut()
}