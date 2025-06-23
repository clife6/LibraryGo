package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.entity.BorrowRecord
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BorrowingRecordsViewModel : ViewModel() {
    // 用于导航的 SharedFlow
    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    // 借阅记录的 StateFlow
    private val _records = MutableStateFlow<List<BorrowRecord.BorrowingRecord>>(emptyList())
    val records = _records.asStateFlow()

    // 加载借阅记录
    fun loadBorrowingRecords(userId: String) {
        viewModelScope.launch {
            try {
                val borrowingRecordsResponse = RetrofitClient.borrowRecordService.searchBorrowRecordsByUserId(userId)
                if(borrowingRecordsResponse.isSuccessful){
                    borrowingRecordsResponse.body()?.let {
                        if (it.code == 1) {
                            _records.value = it.borrowRecords // 成功时更新记录
                        } else {
                            _records.value = emptyList() // 失败时清空记录
                        }
                    } ?: run {
                        _records.value = emptyList() // 响应体为空时清空记录
                    }
                } else {
                    _records.value = emptyList() // 请求失败时清空记录
                }
            } catch (e: Exception) {
                _records.value = emptyList() // 发生错误时清空记录
            }
        }

    }

}