package com.clife.librarygo.data.request

sealed class BorrowRecordRequest {

    data class AddBorrowRecordRequest(
        val bookUid: String,
        val userId: String,
        val borrowDate: String,
        val returnDate: String,
    )

    data class UpdateBorrowRecordRequest(
        val bookUid: String,
        val userId: String,
        val actualReturnDate: String,
    )
}