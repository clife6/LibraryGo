package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.data.request.BorrowRecordRequest
import com.clife.librarygo.data.request.BorrowRequest
import com.clife.librarygo.data.request.CollectionRequest
import com.clife.librarygo.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookDetailViewModel : ViewModel() {
    private val _bookDetail = MutableStateFlow<Book.BookDetail?>(null)
    val bookDetail = _bookDetail.asStateFlow()

    private val _isCollection = MutableStateFlow(false)
    val isCollection = _isCollection.asStateFlow()

    private val _isBorrow = MutableStateFlow(false)
    val isBorrow = _isBorrow.asStateFlow()

    private val _borrowDate = MutableStateFlow("")
    val borrowDate = _borrowDate.asStateFlow()

    private val _returnDate = MutableStateFlow("")
    val returnDate = _returnDate.asStateFlow()

    private val _actualReturnDate = MutableStateFlow("")
    val actualReturnDate = _actualReturnDate.asStateFlow()

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
                if (!response.isSuccessful || response.body() == null) {
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
                val response = RetrofitClient.collectionService.searchCollectionByUserIdAndBookUid(
                    userId,
                    bookUid
                )
                if (!response.isSuccessful || response.body() == null) {
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
                val response = RetrofitClient.collectionService.updateCollection(
                    CollectionRequest.UpdateCollectionRequest(
                        userId = userId,
                        bookUid = bookUid,
                        isCollection = isCollection
                    )
                )
                if (!response.isSuccessful || response.body() == null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                if (isCollection)
                    when (response.body()?.code) {
                        1 -> {}
                        0 -> _bookDetailError.value = "已存在收藏"
                        -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                        else -> _bookDetailError.value = "收藏失败，请稍后再试"
                    }
                else
                    when (response.body()?.code) {
                        1 -> {}
                        0 -> _bookDetailError.value = "没有找到对应的收藏"
                        -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                        else -> _bookDetailError.value = "取消收藏失败，请稍后再试"
                    }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                setIsCollection(userId, bookUid)
                _isLoading.value = false
            }
        }
    }

    fun setIsBorrow(userId: String, bookUid: String) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _isBorrow.value = false
            _isLoading.value = true
            try {
                val response =
                    RetrofitClient.borrowService.searchBorrowByUserIdAndBookUid(userId, bookUid)
                if (!response.isSuccessful || response.body() == null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> _isBorrow.value = true
                    0 -> {}
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

    //确认借阅图书
    fun confirmBorrow(userId: String, bookUid: String) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _isLoading.value = true
            try {
                val response = RetrofitClient.borrowService.borrowBook(
                    BorrowRequest.BorrowBookRequest(
                        userId,
                        bookUid
                    )
                )
                if (!response.isSuccessful || response.body() == null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> {
                        val addBorrowRecordResponse =
                            RetrofitClient.borrowRecordService.addBorrowRecord(
                                BorrowRecordRequest.AddBorrowRecordRequest(
                                    bookUid = bookUid,
                                    userId = userId,
                                    borrowDate = _borrowDate.value,
                                    returnDate = _returnDate.value
                                )
                            )
                        if (!addBorrowRecordResponse.isSuccessful || addBorrowRecordResponse.body() == null) {
                            _bookDetailError.value = "网络请求失败，请稍后再试"
                            _isLoading.value = false
                            return@launch
                        }
                        when (addBorrowRecordResponse.body()?.code) {
                            1 -> {}
                            -1 -> _bookDetailError.value =
                                "用户ID、书籍UID、借书日期或还书日期不能为空"

                            else -> _bookDetailError.value = "添加借阅记录失败，请稍后再试"
                        }
                    }

                    -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                    else -> _bookDetailError.value = "借阅失败，请稍后再试"
                }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                setIsBorrow(userId, bookUid)
                _isLoading.value = false
            }
        }
    }

    //确认归还图书
    fun confirmReturn(userId: String, bookUid: String) {
        viewModelScope.launch {
            _bookDetailError.value = null
            _isLoading.value = true
            try {
                val response = RetrofitClient.borrowService.deleteBorrowRecord(
                    BorrowRequest.DeleteBorrowRecordRequest(
                        userId,
                        bookUid
                    )
                )
                if (!response.isSuccessful || response.body() == null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> {
                        val updateBorrowRecordResponse =
                            RetrofitClient.borrowRecordService.updateBorrowRecord(
                                BorrowRecordRequest.UpdateBorrowRecordRequest(
                                    bookUid = bookUid,
                                    userId = userId,
                                    actualReturnDate = _actualReturnDate.value
                                )
                            )
                        if (!updateBorrowRecordResponse.isSuccessful || updateBorrowRecordResponse.body() == null) {
                            _bookDetailError.value = "网络请求失败，请稍后再试"
                            _isLoading.value = false
                            return@launch
                        }
                        when (updateBorrowRecordResponse.body()?.code) {
                            1 -> {}
                            0 -> _bookDetailError.value = "借阅记录不存在"
                            -1 -> _bookDetailError.value =
                                "用户ID、书籍UID或实际归还日期不能为空"

                            else -> _bookDetailError.value = "更新借阅记录失败，请稍后再试"
                        }
                    }

                    0 -> _bookDetailError.value = "没有找到借阅记录"
                    -1 -> _bookDetailError.value = "用户ID或书籍UID不能为空"
                    else -> _bookDetailError.value = "归还失败，请稍后再试"
                }
            } catch (e: Exception) {
                _bookDetailError.value = "错误信息: ${e.message}"
            } finally {
                setIsBorrow(userId, bookUid)
                _isLoading.value = false
            }
        }
    }

    //获取当前的时间设置借阅时间和还书日期
    fun getBorrowDateAndReturnDate() {
        _bookDetailError.value = null
        _isLoading.value = true
        try {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            _borrowDate.value = currentDateTime.format(formatter)
            _returnDate.value = currentDateTime.plusDays(30).format(formatter)
        } catch (e: Exception) {
            _bookDetailError.value = "错误信息: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    //从后端获取借阅记录的借阅时间和还书日期
    fun getBorrowRecordDates(userId: String, bookUid: String) {
        _bookDetailError.value = null
        _borrowDate.value = ""
        _returnDate.value = ""
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.borrowRecordService.searchBorrowRecord(
                    bookUid = bookUid,
                    userId = userId
                )
                if (!response.isSuccessful || response.body() == null) {
                    _bookDetailError.value = "网络请求失败，请稍后再试"
                    _isLoading.value = false
                    return@launch
                }
                when (response.body()?.code) {
                    1 -> {
                        val borrowRecord = response.body()?.borrowRecord
                        if (borrowRecord != null) {
                            _borrowDate.value = borrowRecord.borrowDate
                            _returnDate.value = borrowRecord.returnDate
                        } else {
                            _bookDetailError.value = "借阅记录不存在"
                        }
                    }

                    0 -> _bookDetailError.value = "借阅记录不存在"
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

    //获取当前的时间设置实际归还时间
    fun getActualReturnDate() {
        _bookDetailError.value = null
        _isLoading.value = true
        try {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            _actualReturnDate.value = currentDateTime.format(formatter)
        } catch (e: Exception) {
            _bookDetailError.value = "错误信息: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun isOverdue(): Boolean {
        if (_returnDate.value.isEmpty() || _actualReturnDate.value.isEmpty()) {
            return false
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val returnDateTime = LocalDateTime.parse(_returnDate.value, formatter)
        val actualReturnDateTime = LocalDateTime.parse(_actualReturnDate.value, formatter)
        return actualReturnDateTime.isAfter(returnDateTime)
    }

    fun clearBookDetailError() {
        _bookDetailError.value = null
    }
}