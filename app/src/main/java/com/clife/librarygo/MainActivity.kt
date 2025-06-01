package com.clife.librarygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.navigation.LibraryNavGraph
import com.clife.librarygo.ui.theme.LibraryGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryGoTheme {
                LibraryNavGraph(navController = rememberNavController())
            }
        }
    }
}