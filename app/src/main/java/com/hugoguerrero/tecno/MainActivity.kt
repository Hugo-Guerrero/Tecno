package com.hugoguerrero.tecno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.hugoguerrero.tecno.ui.navigation.MainNavGraph
import com.hugoguerrero.tecno.ui.theme.TecnoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Anotación añadida
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TecnoTheme {
                val navController = rememberNavController()
                MainNavGraph(navController = navController)
            }
        }
    }
}
