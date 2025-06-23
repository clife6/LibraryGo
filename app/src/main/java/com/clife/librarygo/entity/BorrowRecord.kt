package com.clife.librarygo.entity

sealed class BorrowRecord {
    data class SearchBorrowRecord(
        val borrowDate: String,
        val returnDate: String,
    )

    data class BorrowingRecord(
        val id: String,
        val userId: String,
        val bookUid: String,
        val borrowDate: String,
        val returnDate: String,
        val dueDate: String?,
        val status: String
    )
}