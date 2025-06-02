package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RetrievePasswordViewModel : ViewModel()   {
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 用于显示找回密码错误信息
    private val _retrievePasswordError = MutableStateFlow<String?>(null)
    val retrievePasswordError = _retrievePasswordError.asStateFlow()

    fun verifyAccount(userEmail : String,userPhone : String) {
        viewModelScope.launch{
            _retrievePasswordError.value = null
            val id = "12345" //getUserIdByEmailAndPhone(userEmail, userPhone)
            if(userEmail == "" || userPhone == "") {
                _retrievePasswordError.value = "邮箱或手机号不能为空"
            }
            else if (id != null)//检查账号信息是否匹配
            {
                // 假设验证通过，导航到设置新密码界面
                _navigateTo.emit(Routes.setNewPasswordWithId(id))
            } else {
                // 验证失败，设置错误信息
                _retrievePasswordError.value = "账号信息不匹配，请检查"
            }
        }
    }

    fun clearRetrievePasswordError() {
        _retrievePasswordError.value = null
    }
}