package com.clife.librarygo.data.request

sealed class BorrowRequest{

    data class BorrowBookRequest(
        val userId: String,
        val bookUid: String,
    )
}