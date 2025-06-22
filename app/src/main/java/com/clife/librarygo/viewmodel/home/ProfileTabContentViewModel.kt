package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileTabContentViewModel : ViewModel() {
    // 用于导航的 SharedFlow
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 错误信息的 StateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 图书信息的 StateFlow
    private val _userInfo = MutableStateFlow<User.UserInfo?>(null)
    val userInfo = _userInfo.asStateFlow()

    // 根据用户id获取用户信息
    suspend fun getUserInfo(id: String) {
        val userInfoResponse = RetrofitClient.userService.getUserInfo(id)
        try {
            if (userInfoResponse.isSuccessful){
                userInfoResponse.body()?.let {
                    if (it.code == 1) {
                        _userInfo.value = it.user
                    } else if(it.code == 0) {
                        _errorMessage.value = "用户不存在"
                    }else{
                        _errorMessage.value = it.error ?: "获取用户信息失败"
                    }
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = "网络错误: ${e.message}"
        }
    }
}