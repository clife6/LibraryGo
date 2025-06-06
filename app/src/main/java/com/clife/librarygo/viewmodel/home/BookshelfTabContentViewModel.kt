package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.Job
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

    // 用于控制图书信息的加载状态
    private var fetchJob: Job? = null
    private var fetchSeq = 0 // 请求序号

    // 标志位，表示是否已经加载过书架
    private var isLoaded = false

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
        if (!isLoaded){
            isLoaded = true
            fetchBooks(userId)
        }
    }

    // 刷新书架
    fun refreshBookshelf(userId: String) {
        loadBookshelf(userId)
    }

    // 下拉刷新书架
    fun pullToRefresh(userId: String) {
        fetchBooks(userId)
    }

    // 获取书架中的图书信息
    private fun fetchBooks(userId: String) {
        fetchJob?.cancel()
        val currentSeq = ++fetchSeq
        fetchJob = viewModelScope.launch {
            val tempList = mutableListOf<Book.BookInBookshelf>()
            val uIDS = getBookUid(userId)
            for (uid in uIDS) {
                getBookByUid(uid)?.let { tempList.add(it) }
            }
            // 只处理最新请求的结果
            if (currentSeq == fetchSeq) {
                if (tempList.isNotEmpty()) {
                    _books.value = tempList
                    _errorMessage.value = null
                } else {
                    // 不清空 books，只弹窗
                    _errorMessage.value = "暂无图书"
                }
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}