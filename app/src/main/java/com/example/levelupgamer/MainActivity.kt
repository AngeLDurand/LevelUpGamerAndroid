package com.example.levelupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer.navigation.AppNavHost
import com.example.levelupgamer.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpGamerTheme(darkTheme = true, dynamicColor = false) {
                val nav = rememberNavController()
                AppNavHost(nav)
            }
        }
    }
}
