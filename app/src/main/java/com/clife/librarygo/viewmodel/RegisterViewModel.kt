package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel(){
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 用于显示注册错误信息
    private val _registerError = MutableStateFlow<String?>(null)
    val registerError = _registerError.asStateFlow()

    fun register(username: String, password: String){

    }
}