package com.clife.librarygo.viewmodel

import androidx.lifecycle.viewModelScope
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : androidx.lifecycle.ViewModel() {

    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()
    // 用于显示登录错误信息
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    fun login(username: String, password: String): Boolean {
        viewModelScope.launch {
            _loginError.value = null
            if(username!= "admin" || password != "123456") {
                _navigateTo.emit(Routes.HOME)
            } else {
                _loginError.value = "用户名或密码错误"
            }
        }
        return _loginError.value == null // 返回登录是否成功
    }

    // 用于清除错误信息，例如当用户开始重新输入时
    fun clearLoginError() {
        _loginError.value = null
    }

}