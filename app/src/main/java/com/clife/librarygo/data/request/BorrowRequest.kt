package com.clife.librarygo.data.request

sealed class BorrowRequest{

    data class BorrowBookRequest(
        val userId: String,
        val bookUid: String,
    )

    data class DeleteBorrowRecordRequest(
        val userId: String,
        val bookUid: String,
    )
}