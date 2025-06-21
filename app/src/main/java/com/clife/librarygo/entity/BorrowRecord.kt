package com.clife.librarygo.entity

sealed class BorrowRecord {

    data class SearchBorrowRecord(
        val borrowDate: String,
        val returnDate: String,
    )
}