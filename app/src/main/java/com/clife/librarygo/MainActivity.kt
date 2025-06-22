package com.clife.librarygo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.navigation.LibraryNavGraph
import com.clife.librarygo.service.DueCheckService
import com.clife.librarygo.ui.theme.LibraryGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            AppLifecycleObserver(this, DueCheckService::class.java)
        )
        setContent {
            LibraryGoTheme {
                LibraryNavGraph(navController = rememberNavController())
            }
        }
    }
}

class AppLifecycleObserver(
    private val context: Context,
    private val serviceClass: Class<*>
) : DefaultLifecycleObserver {

    @SuppressLint("ImplicitSamInstance")
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        context.stopService(Intent(context, serviceClass))
    }
}