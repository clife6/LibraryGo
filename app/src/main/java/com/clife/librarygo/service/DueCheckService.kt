package com.clife.librarygo.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Calendar

class DueCheckService : Service() {
    override fun onBind(intent: Intent?) = null

    private var userId: String? = null

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()
        Log.d("DueCheckService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        userId = intent?.getStringExtra("USER_ID")
        setupDailyCheckAlarm()
        sendImmediateDueCheckBroadcast()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DueCheckService", "Service destroyed")
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun setupDailyCheckAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent("ACTION_CHECK_DUE_BOOKS").apply {
            setPackage(this@DueCheckService.packageName)
            putExtra("USER_ID", userId)
        }
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 设置每天的检查时间
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        // 检查是否有精确闹钟权限
        if (!alarmManager.canScheduleExactAlarms()) {
            Log.w("DueCheckService", "需要精确闹钟权限")
            requestExactAlarmPermission()
            return
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d(
            "DueCheckService",
            "每日检查闹钟已设置，下次触发时间: ${
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.time)
            }"
        )
    }

    private fun requestExactAlarmPermission() {
        val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = "package:${packageName}".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun sendImmediateDueCheckBroadcast() {
        val intent = Intent("ACTION_CHECK_DUE_BOOKS").apply {
            setPackage(this@DueCheckService.packageName)
            putExtra("USER_ID", userId)
        }
        sendBroadcast(intent)
    }
}