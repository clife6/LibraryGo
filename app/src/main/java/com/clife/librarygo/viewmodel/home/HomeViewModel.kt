package com.clife.librarygo.viewmodel.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTabIndex.value = index
    }
}