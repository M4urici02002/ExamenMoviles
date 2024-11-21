package com.example.kotlin.wushuapp.utils

import android.app.Application
import com.example.kotlin.wushuapp.data.network.NetworkModuleDI

class ExamenMoviles : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModuleDI.initializeParse(this)
    }
}
