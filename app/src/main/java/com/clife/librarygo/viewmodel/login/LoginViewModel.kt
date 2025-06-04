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

class LoginViewModel() : ViewModel(){
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 表单状态
    data class LoginFormState(
        val username: String = "",
        val usernameError : Boolean = false,
        val usernameErrorMessage: String = "",
        val password: String = "",
        val passwordError: Boolean = false,
        val passwordErrorMessage: String = "",
        val loginError: Boolean = false,
        val loginErrorMessage: String = ""
    )
    private val _formState = MutableStateFlow(LoginFormState())
    val formState = _formState.asStateFlow()

    // 更新表单
    // 更新用户名
    fun updateUsername(username: String) {
        _formState.value = _formState.value.copy(username = username)
    }

    // 更新密码
    fun updatePassword(password: String) {
        _formState.value = _formState.value.copy(
            password = password,
            loginError = false,
            loginErrorMessage = ""
        )
    }

    //登录
    fun login(): Boolean {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userService.loginUser(
                    UserRequest.LoginUserRequest(_formState.value.username, _formState.value.password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        when (it.code) {
                            1 -> _navigateTo.emit(Routes.homeWithId("1")) // 登录成功，导航到主页
                            0 -> _formState.value = _formState.value.copy(
                                loginError = true,
                                loginErrorMessage = "账号或密码错误"
                            )
                            else -> _formState.value = _formState.value.copy(
                                loginError = true,
                                loginErrorMessage = "账号密码不能为空"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 处理网络错误或其他异常
                _formState.value = _formState.value.copy(
                    loginError = true,
                    loginErrorMessage = "网络错误，请稍后再试"
                )
            }
        }
        return _formState.value.loginError // 返回登录是否成功
    }
}