package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverTabContentViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _count = MutableStateFlow(-1)
    val count = _count.asStateFlow()

    private var _books = MutableStateFlow<List<Book.BookInSearch>>(emptyList())
    val books = _books.asStateFlow()

    private val _discoverTabContentError = MutableStateFlow<String?>(null)
    val discoverTabContentError = _discoverTabContentError.asStateFlow()

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _discoverTabContentError.value = null
            _isLoading.value = true
            _count.value = -1
            _books.value = emptyList()
            try {
                if (query.isBlank()) {
                    _discoverTabContentError.value = "搜索关键词不能为空"
                    _isLoading.value = false
                    return@launch
                }
                val response = RetrofitClient.bookService.searchBookByTitleOrAuthor(query)
                if (!response.isSuccessful || response.body() == null) {
                    _discoverTabContentError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> {
                        _count.value = response.body()?.count ?: 0
                        _books.value = response.body()?.books ?: emptyList()
                    }

                    0 -> _count.value = 0
                    -1 -> _discoverTabContentError.value = "搜索关键词不能为空"
                    else -> _discoverTabContentError.value = "查询失败，请稍后再试"
                }
            } catch (e: Exception) {
                _discoverTabContentError.value = "错误信息: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearRetrievePasswordError() {
        _discoverTabContentError.value = null
    }
}