package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.navigation.Routes

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(){
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()
    // 用于显示登录错误信息
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    fun login(username: String, password: String): Boolean {
        viewModelScope.launch {
            _loginError.value = null
            try {
                val response = RetrofitClient.userService.searchUser(username)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(it.code == 1){
                            _navigateTo.emit(Routes.HOME) // 登录成功，导航到主页
                        }
                    }
                } else {
                    // 登录失败，设置错误信息
                    _loginError.value = "用户名或密码错误"
                }
            } catch (e: Exception) {
                _loginError.value = "登录失败: ${e.message}"
            }
        }
        return _loginError.value == null // 返回登录是否成功
    }

    // 用于清除错误信息，例如当用户开始重新输入时
    fun clearLoginError() {
        _loginError.value = null
    }

}