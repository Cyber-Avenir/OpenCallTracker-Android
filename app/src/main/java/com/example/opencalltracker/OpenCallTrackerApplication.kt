package com.example.opencalltracker

import android.app.Application
import com.example.opencalltracker.di.AppContainer

class OpenCallTrackerApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
