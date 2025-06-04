package com.clife.librarygo.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RetrievePasswordViewModel : ViewModel() {
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    private val _retrievePasswordError = MutableStateFlow<String?>(null)
    val retrievePasswordError = _retrievePasswordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun verifyAccount(username: String, userEmail: String, userPhone: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _retrievePasswordError.value = null
            // 输入验证
            if (username.isBlank() || userEmail.isBlank() || userPhone.isBlank()) {
                _retrievePasswordError.value = "账户名称、邮箱或手机号不能为空"
                _isLoading.value = false
                return@launch
            }
            try {
                // 1. 检查用户是否存在
                val searchUserResponse = RetrofitClient.userService.searchUser(username)
                if (!searchUserResponse.isSuccessful || searchUserResponse.body() == null) {
                    _retrievePasswordError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (searchUserResponse.body()?.code) {
                    1 -> {
                        // 用户存在，验证邮箱和手机号
                        try {
                            val checkResponse = RetrofitClient.userService.checkUserEmailAndPhone(username, userEmail, userPhone)
                            if (!checkResponse.isSuccessful || checkResponse.body() == null) {
                                _retrievePasswordError.value = "网络请求失败，请稍后再试"
                                _isLoading.value = false
                                return@launch
                            }
                            when (checkResponse.body()?.code) {
                                1 -> _navigateTo.emit(Routes.setNewPasswordWithId(checkResponse.body()?.id.toString()))
                                0 -> _retrievePasswordError.value = "邮箱或手机号与账户不匹配"
                                else -> _retrievePasswordError.value = "账户验证失败"
                            }
                        } catch (e: Exception) {
                            _retrievePasswordError.value = "错误信息: ${e.message}"
                        }
                    }
                    0 -> _retrievePasswordError.value = "未找到该账户，请检查账户名称"
                    else -> _retrievePasswordError.value = "账户查询失败"
                }
            } catch (e: Exception) {
                _retrievePasswordError.value = "错误信息: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun clearRetrievePasswordError() {
        _retrievePasswordError.value = null
    }
}