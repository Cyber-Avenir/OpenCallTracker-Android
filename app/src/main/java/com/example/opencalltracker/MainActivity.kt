package com.example.opencalltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.opencalltracker.ui.main.MainScreen
import com.example.opencalltracker.ui.theme.OpenCallTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenCallTrackerTheme {
                MainScreen()
            }
        }
    }
}
