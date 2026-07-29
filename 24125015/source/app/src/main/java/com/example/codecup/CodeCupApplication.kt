package com.example.codecup

import android.app.Application
import com.example.codecup.data.AppContainer
import com.example.codecup.data.DemoDataSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CodeCupApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DemoDataSeeder(container.database, container.rewardsRepository).seedIfNeeded()
        }
    }
}
