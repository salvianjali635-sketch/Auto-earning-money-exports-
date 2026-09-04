package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.UserPreferencesRepository
import com.example.util.NetworkMonitor

class AutoEarnApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: UserPreferencesRepository by lazy { UserPreferencesRepository(this) }
    val networkMonitor: NetworkMonitor by lazy { NetworkMonitor(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
