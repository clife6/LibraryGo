package com.clife.librarygo.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.clife.librarygo.R
import com.clife.librarygo.data.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DueCheckReceiver : BroadcastReceiver() {
    private var userId: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_CHECK_DUE_BOOKS") {
            userId = intent.getStringExtra("USER_ID")
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                return
            }
            createNotificationChannel(context)
            CoroutineScope(Dispatchers.IO).launch {
                userId?.let { countBorrowByUserId(it, context) }
            }
        }
    }

    private suspend fun countBorrowByUserId(userId: String, context: Context) {
        try {
            val response = RetrofitClient.borrowService.countBorrowByUserId(userId)
            if (!response.isSuccessful || response.body() == null) {
                Log.e(
                    "DueCheckReceiver",
                    "Failed to count borrowed books for user $userId: ${
                        response.errorBody()?.string()
                    }"
                )
                return
            }
            when (response.body()?.code) {
                1 -> {
                    val borrowCount = response.body()?.borrowCount ?: 0
                    sendNotification(
                        context,
                        "借阅检查",
                        "您当前正在借阅 $borrowCount 本书",
                        R.drawable.logo
                    )
                }

                else -> {
                    Log.e(
                        "DueCheckReceiver",
                        "Error counting borrowed books for user $userId: ${response.body()?.error}"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(
                "DueCheckReceiver",
                "Error counting borrowed books for user $userId: ${e.message}"
            )
        }
    }

    private fun sendNotification(context: Context, title: String, message: String, iconResId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        val name = "借阅检查通知"
        val descriptionText = "借阅检查结果通知"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "due_check_channel"
        private const val NOTIFICATION_ID = 1
    }
}