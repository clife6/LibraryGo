package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DiscoverTabContentViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _Books = MutableStateFlow<List<Book.BookInSearch>>(emptyList())
    val Books = _Books.asStateFlow()

    fun searchBooks(query: String) {
        _isLoading.value = true
        // 这里可以添加实际的搜索逻辑，比如调用API获取数据
        // 目前使用静态数据模拟
        _isLoading.value = false
    }


}