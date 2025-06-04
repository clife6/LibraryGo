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

class SetNewPasswordViewModel : ViewModel() {

    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    private val _setNewPasswordError = MutableStateFlow<String?>(null)
    val setNewPasswordError = _setNewPasswordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setNewPassword(id: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch{
            _isLoading.value = true
            _setNewPasswordError.value = null
            // 简单的密码匹配检查
            if(newPassword == "" || confirmPassword == "") {
                _setNewPasswordError.value = "密码不能为空"
                _isLoading.value = false
                return@launch
            }
            if(newPassword != confirmPassword) {
                _setNewPasswordError.value = "两次输入的密码不一致"
                _isLoading.value = false
                return@launch
            }
            try {
                val response = RetrofitClient.userService.setNewPassword(UserRequest.SetNewPasswordRequest(id, newPassword))
                if (!response.isSuccessful || response.body() == null) {
                    _setNewPasswordError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> _navigateTo.emit(Routes.LOGIN)
                    0 -> _setNewPasswordError.value = "账户不存在或已被删除"
                    else -> _setNewPasswordError.value = "修改密码失败"
                }
            }catch (e: Exception) {
                // 捕获异常并设置错误信息
                _setNewPasswordError.value = "错误信息: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSetNewPasswordError() {
        _setNewPasswordError.value = null
    }
}