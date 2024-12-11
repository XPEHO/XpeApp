package com.xpeho.xpeapp.di

// Prevent circular dependencies between AuthManager and WordpressRepo
class TokenProvider {
    private var token: String? = null

    fun get(): String? {
        return this.token
    }

    fun set(token: String) {
        this.token = token
    }
}