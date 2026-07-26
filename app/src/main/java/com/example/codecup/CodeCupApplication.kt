package com.example.codecup

import android.app.Application
import com.example.codecup.data.AppContainer

class CodeCupApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
