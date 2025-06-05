package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfTabContentViewModel : ViewModel() {

    // 错误信息的 StateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 图书信息的 StateFlow
    private val _books = MutableStateFlow<List<Book.BookInBookshelf>>(emptyList())
    val books = _books.asStateFlow()

    var bookList = mutableListOf<Book.BookInBookshelf>() // 可变列表用于暂时存储书架中的图书信息

    // 加载借阅图书的Uid
    suspend fun getBookUid(userId: String): List<String> {
        return try {
            val borrowResponse = RetrofitClient.bookService.searchBorrow(userId)
            if (borrowResponse.isSuccessful) {
                borrowResponse.body()?.let {
                    if (it.code == 1) {
                        it.books?.map { book -> book.uid } ?: emptyList()
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
                    }else{
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

    // 加载书架中的图书信息
    fun loadBookshelf(userId: String) {
        viewModelScope.launch {
            bookList.clear()
            val uIDS = getBookUid(userId)
            for (uid in uIDS) {
                getBookByUid(uid)?.let { bookList.add(it) }
            }
            _books.value = bookList.toList()
            if (bookList.isEmpty()) {
                _errorMessage.value = "暂无图书"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}