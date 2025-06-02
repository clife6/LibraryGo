package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setNewPassword(uuid: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch{
            _setNewPasswordError.value = null
            // 简单的密码匹配检查
            if(newPassword == "" || confirmPassword == "") {
                _setNewPasswordError.value = "密码不能为空"
            }
            else if (newPassword == confirmPassword) {
                // 假设设置新密码成功，导航到登录界面
                _navigateTo.emit(Routes.LOGIN)
            } else {
                // 密码不匹配，设置错误信息
                _setNewPasswordError.value = "两次输入的密码不一致"
            }
        }
    }

    fun clearSetNewPasswordError() {
        _setNewPasswordError.value = null
    }
}