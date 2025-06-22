package com.clife.librarygo.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.util.Log
import com.clife.librarygo.broadcasereceiver.DueCheckReceiver

class DueCheckService : Service() {
    override fun onBind(intent: Intent?) = null

    private var userId: String? = null

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()
        Log.d("DueCheckService", "Service created")
        setupDailyCheckAlarm()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        userId = intent?.getStringExtra("USER_ID")
        Log.d("DueCheckService", "Service started with user ID: $userId")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DueCheckService", "Service destroyed")
    }

    private fun setupDailyCheckAlarm() {
        // 获取系统闹钟服务，用于设置定时任务
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        // 创建用于广播的Intent，指向DueCheckReceiver接收器
        val intent = Intent(this, DueCheckReceiver::class.java).apply {
            // 设置Action标识，用于接收器识别这是检查到期图书的操作
            action = "ACTION_CHECK_DUE_BOOKS"
        }
        // 创建用于延迟执行的PendingIntent，将在指定时间触发广播
        pendingIntent = PendingIntent.getBroadcast(
            // 上下文对象
            this,
            // 请求码，用于区分不同的PendingIntent，这里是0表示唯一请求
            0,
            // 要执行的Intent
            intent,
            // 标志位：如果PendingIntent已存在则更新 + 设置为不可变（Android安全要求）
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 设置每天的闹钟，计算下次触发时间（当前时间加一天）
        val triggerAtMillis = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY
        // 设置重复闹钟，使用不精确重复模式（省电）
        alarmManager.setInexactRepeating(
            // 类型：RTC_WAKEUP表示设备休眠时也会唤醒系统
            AlarmManager.RTC_WAKEUP,
            // 首次触发的时间
            triggerAtMillis,
            // 重复周期：每天执行一次
            AlarmManager.INTERVAL_DAY,
            // 要执行的PendingIntent
            pendingIntent
        )
    }
}