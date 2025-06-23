package com.clife.librarygo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.broadcastreceiver.DueCheckReceiver
import com.clife.librarygo.navigation.LibraryNavGraph
import com.clife.librarygo.service.DueCheckService
import com.clife.librarygo.ui.theme.LibraryGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {

                @SuppressLint("ImplicitSamInstance")
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    stopService(Intent(this@MainActivity, DueCheckService::class.java))
                    unregisterReceiver(DueCheckReceiver())
                }
            }
        )
        val filter = IntentFilter("ACTION_CHECK_DUE_BOOKS")
        registerReceiver(DueCheckReceiver(), filter, RECEIVER_NOT_EXPORTED)
        Log.d("MainActivity", "Receiver registered")
        setContent {
            LibraryGoTheme {
                LibraryNavGraph(navController = rememberNavController())
            }
        }
    }
}