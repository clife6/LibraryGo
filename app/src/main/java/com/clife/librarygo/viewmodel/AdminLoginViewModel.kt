package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminLoginViewModel : ViewModel()  {
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 用于显示登录错误信息
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    // 模拟的登录逻辑
    fun login(adminName: String, password: String) {
        println("登录")
        viewModelScope.launch {
            // 清除之前的错误信息
            _loginError.value = null
            if (adminName == "" || password == ""){
                _loginError.value = "管理员账号或密码不能为空"
            }
            else if (adminName == "admin" && password == "admin") {
                // 登录成功，导航到主页
                // 假设你有一个 Routes.HOME 定义
                _navigateTo.emit(Routes.HOME)
            }
            else {
                // 登录失败
                _loginError.value = "管理员账号或密码错误"
            }
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }
}