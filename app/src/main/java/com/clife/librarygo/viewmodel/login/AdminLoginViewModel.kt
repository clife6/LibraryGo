package com.clife.librarygo.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.data.request.UserRequest
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
        viewModelScope.launch {
            // 清除之前的错误信息
            _loginError.value = null
            if (adminName == "" || password == "") {
                _loginError.value = "管理员账号或密码不能为空"
                return@launch
            }
            try {
                val response = RetrofitClient.userService.loginAdmin(UserRequest.LoginAdminRequest(adminName, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.code == 1) {
                            // 登录成功，导航到管理员界面
                            _navigateTo.emit(Routes.homeWithId(it.id.toString()))
                        } else if (it.code == 0) {
                            // 登录失败，设置错误信息
                            _loginError.value = "管理员账号或密码错误"
                        } else if (it.code == -1) {
                            // 其他错误，设置错误信息
                            _loginError.value = "登录失败，请稍后再试"
                        }
                    }
                } else {
                    // 请求失败，设置错误信息
                    _loginError.value = "网络请求失败，请稍后再试"
                }
            } catch (e: Exception) {
                // 捕获异常并设置错误信息
                _loginError.value = (_loginError.value ?: "") + "错误信息: ${e.message}"
                return@launch
            }
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }
}