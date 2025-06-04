package com.clife.librarygo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel: ViewModel() {
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // init 块是 Kotlin 类的主构造函数的一部分。
    // 当 WelcomeViewModel 的一个实例被创建时，init 块中的代码会立即执行。
    init {
        viewModelScope.launch {
            delay(2000L) // 延迟 2 秒
            _navigateTo.emit(Routes.LOGIN) // 发射导航到登录页的事件
        }
    }
}