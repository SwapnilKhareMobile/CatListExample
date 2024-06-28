package com.example.catlistexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.catlistexample.ui.CatApp
import com.example.catlistexample.ui.rememberCatAppState
import com.example.catlistexample.ui.theme.CatListExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberCatAppState()
            CatListExampleTheme {
                Scaffold() { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        CatApp(appState)
                    }
                }
            }
        }
    }
}
