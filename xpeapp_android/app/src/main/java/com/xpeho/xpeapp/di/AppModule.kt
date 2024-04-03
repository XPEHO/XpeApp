package com.xpeho.xpeapp.di

import android.content.Context
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.domain.AuthenticationManager

interface AppModule {
    val authenticationManager: AuthenticationManager
    val wordpressRepository: WordpressRepository
    val datastorePref: DatastorePref
}

class MainAppModule(
    private val appContext: Context
) : AppModule {
    override val authenticationManager: AuthenticationManager by lazy {
        AuthenticationManager(
            wordpressRepo = wordpressRepository,
            datastorePref = datastorePref
        )
    }

    override val wordpressRepository: WordpressRepository by lazy {
        WordpressRepository()
    }

    override val datastorePref: DatastorePref by lazy {
        DatastorePref(appContext)
    }
}