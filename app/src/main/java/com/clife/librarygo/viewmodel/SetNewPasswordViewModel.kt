package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.data.entity.SetNewPasswordRequest
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

    fun setNewPassword(id: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch{
            _setNewPasswordError.value = null
            // 简单的密码匹配检查
            if(newPassword == "" || confirmPassword == "") {
                _setNewPasswordError.value = "密码不能为空"
                return@launch
            }
            try {
                val response = RetrofitClient.userService.setNewPassword(SetNewPasswordRequest(id, newPassword))
                if (response.isSuccessful){
                    response.body()?.let {
                        if (it.code == 1) {
                            // 假设设置新密码成功，导航到登录界面
                            _navigateTo.emit(Routes.LOGIN)
                        } else if (it.code == 0) {
                            // 设置新密码失败，设置错误信息
                            _setNewPasswordError.value = "账户不存在，设置新密码失败"
                        }else if (it.code == -1) {
                            // 其他错误，设置错误信息
                            _setNewPasswordError.value = "设置新密码失败"
                        }
                    }
                } else {
                    // 请求失败，设置错误信息
                    _setNewPasswordError.value = "网络请求失败，请稍后再试"
                }
            }catch (e: Exception) {
                // 捕获异常并设置错误信息
                _setNewPasswordError.value = (_setNewPasswordError.value ?: "") + "错误信息: ${e.message}"
            }
        }
    }

    fun clearSetNewPasswordError() {
        _setNewPasswordError.value = null
    }
}