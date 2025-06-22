package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfTabContentViewModel : ViewModel() {
    // 用于导航的 SharedFlow
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 错误信息的 StateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 图书信息的 StateFlow
    private val _books = MutableStateFlow<List<Book.BookInBookshelf>>(emptyList())
    val books = _books.asStateFlow()


    // 加载借阅图书的Uid
    suspend fun getBookUid(userId: String): List<String> {
        return try {
            val borrowResponse = RetrofitClient.borrowService.searchBorrow(userId)
            if (borrowResponse.isSuccessful) {
                borrowResponse.body()?.let {
                    if (it.code == 1) {
                        it.books?.map { book -> book.book_uid } ?: emptyList()
                    } else {
                        _errorMessage.value = it.error ?: "获取借阅信息失败"
                        emptyList()
                    }
                } ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "网络错误"
            emptyList()
        }
    }

    // 通过图书Uid获取图书信息
    suspend fun getBookByUid(uid: String): Book.BookInBookshelf? {
        return try {
            val bookResponse = RetrofitClient.bookService.searchBookByUid(uid)
            if (bookResponse.isSuccessful) {
                bookResponse.body()?.let {
                    if (it.code == 1) {
                        it.book
                    } else {
                        _errorMessage.value = it.error ?: "获取图书信息失败"
                        null
                    }
                }
            } else null
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "网络错误"
            null
        }
    }

    // 刷新书架
    fun refresh(userId: String) {
        _books.value = emptyList() // 清空当前书架
        fetchBooks(userId)
    }

    // 获取书架中的图书信息
    private fun fetchBooks(userId: String) {
        viewModelScope.launch {
            try {
                val bookUids = getBookUid(userId)
                if (bookUids.isEmpty()) {
                    _errorMessage.value = "暂无图书"
                    return@launch
                }

                val bookList = bookUids.mapNotNull { uid ->
                    getBookByUid(uid)
                }

                _books.value = bookList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "网络错误"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}