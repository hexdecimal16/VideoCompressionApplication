package com.dhairytripathi.videocompressionapplication.application

import android.app.Application
import android.content.Context

class VideoCompressionApplication : Application() {

    companion object {

        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }
}