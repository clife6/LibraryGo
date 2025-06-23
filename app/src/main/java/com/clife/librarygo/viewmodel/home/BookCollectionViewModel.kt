package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.Book
import com.clife.librarygo.entity.Collection.CollectionInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookCollectionViewModel : ViewModel() {

    // 收藏信息
    private val _collectionList = MutableStateFlow<List<CollectionInfo>>(emptyList())
//    val collectionList = _collectionList.asStateFlow()

    // 书籍信息
    private var _books = MutableStateFlow<List<Book.BookInBookshelf>>(emptyList())
    val books = _books.asStateFlow()

    // 查询结果数量
    private val _count = MutableStateFlow(-1)
    val count = _count.asStateFlow()

    private val _collectionErrorMessage = MutableStateFlow<String?>(null)
    val collectionErrorMessage = _collectionErrorMessage.asStateFlow()

    private val _bookErrorMessage = MutableStateFlow<String?>(null)
    val bookErrorMessage = _bookErrorMessage.asStateFlow()

    private val _bookUid = MutableStateFlow<List<String>>(emptyList())
    val bookUid = _bookUid.asStateFlow()

    // 根据用户ID查询收藏
    suspend fun searchCollectionByUserId(userId: String) {
        val response = RetrofitClient.collectionService.getCollectionByUserId(userId)
        if (response.isSuccessful && response.body() != null) {
            when (response.body()?.code) {
                1 -> {
                    _collectionList.value = response.body()?.collectionList ?: emptyList()
                    _count.value = _collectionList.value.size

                    _bookUid.value = _collectionList.value.map { it.bookUid }
                    fetchBooks()
                }
                -1 -> {
                    _collectionList.value = emptyList()
                    _count.value = 0
                    _collectionErrorMessage.value = response.body()?.error ?: "查询失败，请稍后再试"
                }
                else -> {
                    _collectionList.value = emptyList()
                    _count.value = -1
                    _collectionErrorMessage.value = response.body()?.error ?: "查询失败，请稍后再试"
                }
            }
        } else {
            _collectionList.value = emptyList()
            _count.value = -1
            _collectionErrorMessage.value = "网络请求失败，请稍后再试"
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
                        _bookErrorMessage.value = it.error ?: "获取图书信息失败"
                        null
                    }
                }
            } else null
        } catch (e: Exception) {
            _bookErrorMessage.value = e.message ?: "网络错误"
            null
        }
    }

    // 获取图书信息
    fun fetchBooks() {
        viewModelScope.launch {
            try {
                if (bookUid.value.isEmpty()) {
                    _bookErrorMessage.value = "暂无图书"
                    return@launch
                }
                val bookList = bookUid.value.mapNotNull { uid ->
                    getBookByUid(uid)
                }

                _books.value = bookList
            } catch (e: Exception) {
                _bookErrorMessage.value = e.message ?: "网络错误"
            }
        }
    }
}