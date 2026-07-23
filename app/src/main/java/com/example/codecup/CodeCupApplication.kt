package com.example.codecup

import android.app.Application
import com.example.codecup.data.database.AppDatabase

class CodeCupApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
