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

    fun verifyAccount(userEmail : String,userPhone : String) {
        viewModelScope.launch{
            _retrievePasswordError.value = null
            if(userEmail == "" || userPhone == "") {
                _retrievePasswordError.value = "邮箱或手机号不能为空"
                return@launch
            }
            try {
                val id = RetrofitClient.userService.checkUserEmailAndPhone(userEmail, userPhone)
                if(id.isSuccessful){
                    id.body()?.let {
                        if (it.code == 1) {
                            _navigateTo.emit(Routes.setNewPasswordWithId(it.id.toString()))
                        }else if (it.code == 0) {
                            // 账户不存在，设置错误信息
                            _retrievePasswordError.value = "未查询到邮箱和手机号绑定一致的账户，请检查邮箱或手机号是否正确"
                        } else if (it.code == -1) {
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