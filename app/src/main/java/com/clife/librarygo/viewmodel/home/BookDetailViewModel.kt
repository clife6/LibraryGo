package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.data.request.CollectionRequest
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {
    private val _bookDetail = MutableStateFlow<Book.BookDetail?>(null)
    val bookDetail = _bookDetail.asStateFlow()

    private val _isCollection = MutableStateFlow(false)
    val isCollection = _isCollection.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _bookDetailError = MutableStateFlow<String?>(null)
    val bookDetailError = _bookDetailError.asStateFlow()

    fun setBookDetail(uid: String) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _bookDetail.value = null
            _isLoading.value = true
            try {
                val response = RetrofitClient.bookService.searchBookDetailByUid(uid)
                if (!response.isSuccessful && response.body() != null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> _bookDetail.value = response.body()?.book
                    0 -> _bookDetailError.value = "未找到相关书籍信息"
                    -1 -> _bookDetailError.value = "书籍uid不能为空"
                    else -> _bookDetailError.value = "查询失败，请稍后再试"
                }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setIsCollection(userId: String, bookUid: String) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _isCollection.value = false
            _isLoading.value = true
            try {
                val response = RetrofitClient.collectionService.searchCollectionByUserIdAndBookUid(userId, bookUid)
                if (!response.isSuccessful && response.body() != null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> _isCollection.value = true
                    0 -> _bookDetailError.value = "未找到相关收藏信息"
                    -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                    else -> _bookDetailError.value = "查询失败，请稍后再试"
                }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCollection(userId: String, bookUid: String, isCollection: Boolean) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _isLoading.value = true
            try {
                val response = RetrofitClient.collectionService.updateCollection(CollectionRequest.UpdateCollectionRequest(userId = userId, bookUid = bookUid, isCollection = isCollection))
                if (!response.isSuccessful && response.body() != null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                if(isCollection)
                    when(response.body()?.code) {
                        1 -> setIsCollection(userId, bookUid)
                        0 -> _bookDetailError.value = "已存在收藏"
                        -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                        else -> _bookDetailError.value = "收藏失败，请稍后再试"
                    }
                else
                    when(response.body()?.code) {
                        1 -> setIsCollection(userId, bookUid)
                        0 -> _bookDetailError.value = "没有找到对应的收藏"
                        -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                        else -> _bookDetailError.value = "取消收藏失败，请稍后再试"
                    }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearBookDetailError() {
        _bookDetailError.value = null
    }
}