package com.example.codecup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codecup.data.AppTheme
import com.example.codecup.data.UserPreferencesRepository
import com.example.codecup.ui.navigation.NavGraph
import com.example.codecup.ui.theme.CodeCupTheme
import com.example.codecup.ui.viewmodels.MainViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val userPreferencesRepository = UserPreferencesRepository.getInstance(context)
            val mainViewModel: MainViewModel = viewModel(
                factory = ViewModelFactory(userPreferencesRepository = userPreferencesRepository)
            )
            val themeMode by mainViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                AppTheme.SYSTEM -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }

            CodeCupTheme(darkTheme = darkTheme) {
                NavGraph()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodeCupTheme {
        Greeting("Android")
    }
}