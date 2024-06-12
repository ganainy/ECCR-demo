package com.example.eccr_demo

import DemoApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.marsphotos.ui.theme.EccrdemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EccrdemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    DemoApp()
                }
            }
        }
    }
}
