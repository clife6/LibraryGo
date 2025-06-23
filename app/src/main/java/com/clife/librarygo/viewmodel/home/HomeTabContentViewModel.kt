package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeTabContentViewModel: ViewModel()  {
    // 书籍信息
    private var _books = MutableStateFlow<List<Book.BookInSearch>>(emptyList())
    val books = _books.asStateFlow()

    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    suspend fun getBooks(){
        val response = RetrofitClient.bookService.getRandomBooks(8)
        if( response.isSuccessful && response.body() != null) {
            when (response.body()?.code) {
                1 -> {
                    _books.value = response.body()?.books?: emptyList()
                }
                else -> {
                    _books.value = emptyList()
                    _errorMessage.value = response.body()?.error ?: "查询失败，请稍后再试"
                }
            }
        } else {
            _books.value = emptyList()
            _errorMessage.value = "网络错误，请稍后再试"
        }
    }
}