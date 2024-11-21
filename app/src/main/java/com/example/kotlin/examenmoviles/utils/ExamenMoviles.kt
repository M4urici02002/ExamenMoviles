package com.example.kotlin.examenmoviles.utils

import android.app.Application
import com.example.kotlin.examenmoviles.data.network.NetworkModuleDI

class ExamenMoviles : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModuleDI.initializeParse(this)
    }
}
