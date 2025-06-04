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

class RetrievePasswordViewModel : ViewModel()   {
    // 用于触发导航事件
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 用于显示找回密码错误信息
    private val _retrievePasswordError = MutableStateFlow<String?>(null)
    val retrievePasswordError = _retrievePasswordError.asStateFlow()

    fun verifyAccount(username : String,userEmail : String,userPhone : String) {
        viewModelScope.launch{
            _retrievePasswordError.value = null
            if(username == "" || userEmail == "" || userPhone == "") {
                _retrievePasswordError.value = "账户名称、邮箱或手机号不能为空"
                return@launch
            }
            try {
                val searchUserResponse = RetrofitClient.userService.searchUser(username)
                if (searchUserResponse.isSuccessful) {
                    searchUserResponse.body()?.let { response ->
                        if (response.code == 1) {
                            // 用户名存在，继续验证邮箱和手机号
                            try {
                                val checkUserEmailAndPhoneResponse = RetrofitClient.userService.checkUserEmailAndPhone(username, userEmail, userPhone)
                                if (checkUserEmailAndPhoneResponse.isSuccessful) {
                                    checkUserEmailAndPhoneResponse.body()?.let { checkResponse ->
                                        if (checkResponse.code == 1) {
                                            // 邮箱和手机号匹配，导航到设置新密码界面
                                            _navigateTo.emit(Routes.setNewPasswordWithId(checkResponse.id.toString()))
                                        } else if (checkResponse.code == 0) {
                                            // 邮箱或手机号不匹配，设置错误信息
                                            _retrievePasswordError.value = "邮箱或手机号与账户名称不匹配，请检查输入"
                                        } else if (checkResponse.code == -1) {
                                            // 其他错误，设置错误信息
                                            _retrievePasswordError.value = "账户查询失败"
                                        }
                                    }
                                } else {
                                    // 请求失败，设置错误信息
                                    _retrievePasswordError.value = "网络请求失败，请稍后再试"
                                }
                            }catch (e: Exception) {
                                // 捕获异常并设置错误信息
                                _retrievePasswordError.value = (_retrievePasswordError.value ?: "") + "错误信息: ${e.message}"
                                return@launch
                            }
                        } else if (response.code == 0) {
                            // 用户名不存在，设置错误信息
                            _retrievePasswordError.value = "未查询到该账户，请检查账户名称是否正确"
                        } else if (response.code == -1) {
                            // 其他错误，设置错误信息
                            _retrievePasswordError.value = "账户查询失败"
                        }
                    }
                } else {
                    // 请求失败，设置错误信息
                    _retrievePasswordError.value = "网络请求失败，请稍后再试"
                }
            }catch (e: Exception) {
                _retrievePasswordError.value = (_retrievePasswordError.value ?: "") + "错误信息: ${e.message}"
                return@launch
            }
        }
    }

    fun clearRetrievePasswordError() {
        _retrievePasswordError.value = null
    }
}