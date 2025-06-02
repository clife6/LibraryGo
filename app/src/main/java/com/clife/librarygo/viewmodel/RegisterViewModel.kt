package com.clife.librarygo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.local.AppDatabase
import com.clife.librarygo.data.local.entity.UserEntity
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

 class RegisterViewModel : androidx.lifecycle.ViewModel() {

    // 导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 表单状态
    data class RegisterFormState(
        val username: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val email: String = "",
        val phone: String = "",
        val usernameError: Boolean = false,
        val usernameErrorMessage: String = "",
        val passwordVisible: Boolean = false,
        val confirmPasswordVisible: Boolean = false,
        val passwordError: Boolean = false,
        val passwordErrorMessage: String = "",
        val emailError: Boolean = false,
        val emailErrorMessage: String = "",
        val phoneError: Boolean = false,
        val phoneErrorMessage: String = "",
        val formError: Boolean = false
    )

    private val _formState = MutableStateFlow(RegisterFormState())//MutableStateFlow是一个可变的状态流，用于存储表单状态
    val formState = _formState.asStateFlow()//asStateFlow()将MutableStateFlow转换为不可变的状态流，供外部观察

    // 更新表单字段
    fun updateUsername(username: String) {
        _formState.value = _formState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _formState.value = _formState.value.copy(
            password = password,
            passwordError = false,
            passwordErrorMessage = ""
        )
        validatePasswords()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _formState.value = _formState.value.copy(
            confirmPassword = confirmPassword,
            passwordError = false,
            passwordErrorMessage = ""
        )
        validatePasswords()
    }

    fun updateEmail(email: String) {
        _formState.value = _formState.value.copy(
            email = email,
            emailError = false,
            emailErrorMessage = ""
        )
        validateEmail()
    }

    fun updatePhone(phone: String) {
        if (phone.all { it.isDigit() }) {
            _formState.value = _formState.value.copy(
                phone = phone,
                phoneError = false,
                phoneErrorMessage = ""
            )
            validatePhone()
        }
    }

    fun togglePasswordVisibility() {
        _formState.value = _formState.value.copy(
            passwordVisible = !_formState.value.passwordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _formState.value = _formState.value.copy(
            confirmPasswordVisible = !_formState.value.confirmPasswordVisible
        )
    }

    // 校验逻辑
    private fun validatePasswords() {
        val state = _formState.value
        val result = when {
            state.password.isEmpty() && state.confirmPassword.isEmpty() ->
                false to ""
            state.password != state.confirmPassword ->
                true to "两次输入的密码不一致"
            state.password.length < 6 ->
                true to "密码长度至少为6位"
            else -> false to ""
        }
        _formState.value = state.copy(
            passwordError = result.first,
            passwordErrorMessage = result.second
        )
    }

    private fun validateEmail() {
        val email = _formState.value.email
        val result = when {
            email.isEmpty() -> true to "邮箱不能为空"
            !email.contains("@") -> true to "邮箱格式不正确，缺少@符号"
            email.split("@").last().isEmpty() || !email.split("@").last().contains(".") -> true to "邮箱格式不正确"
            else -> false to ""
        }
        _formState.value = _formState.value.copy(
            emailError = result.first,
            emailErrorMessage = result.second
        )
    }

    private fun validatePhone() {
        val phone = _formState.value.phone
        val result = when {
            phone.isEmpty() -> true to "电话号码不能为空"
            phone.length != 11 -> true to "电话号码必须是11位数字"
            !phone.all { it.isDigit() } -> true to "电话号码必须为纯数字"
            else -> false to ""
        }
        _formState.value = _formState.value.copy(
            phoneError = result.first,
            phoneErrorMessage = result.second
        )
    }

    // 注册逻辑
    fun register() {
        validatePasswords()
        validateEmail()
        validatePhone()
        val state = _formState.value
        val hasError = state.passwordError || state.emailError || state.phoneError
        _formState.value = state.copy(formError = hasError)
        if (!hasError) {
            viewModelScope.launch {
                // 只在注册时检查用户名是否存在
                if (true) { // 这里的ture是为了模拟一个异步操作，实际应用中应该调用数据库查询
                    _formState.value = _formState.value.copy(
                        usernameError = true,
                        usernameErrorMessage = "用户名已存在",
                        formError = true
                    )
                    return@launch //@launch是协程的作用域，表示在这个作用域内执行的代码
                }
                val newUser = UserEntity(
                    id = 0,
                    username = state.username,
                    password = state.password,
                    email = state.email,
                    phone = state.phone,
                    role = "user",
                    createdAt = System.currentTimeMillis(),
                    avatarUrl = null // 头像可以设置为null，表示没有上传头像
                )

                _formState.value = state.copy(formError = false)
                _navigateTo.emit(Routes.LOGIN)
            }
        }
    }
}