package com.clife.librarygo.broadcasereceiver

import android.content.BroadcastReceiver

class DueCheckReceiver : BroadcastReceiver() {
    override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
        // 处理接收到的广播
        if (intent.action == "ACTION_CHECK_DUE_BOOKS") {
            // 在这里执行检查到期图书的逻辑
            // 例如，调用相关服务或方法来检查图书到期情况
        }
    }
}