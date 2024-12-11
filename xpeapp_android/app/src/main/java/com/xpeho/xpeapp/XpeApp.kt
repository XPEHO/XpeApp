package com.xpeho.xpeapp

import android.app.Application
import com.xpeho.xpeapp.di.AppModule
import com.xpeho.xpeapp.di.MainAppModule

class XpeApp : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = MainAppModule(appContext = this)
    }
}