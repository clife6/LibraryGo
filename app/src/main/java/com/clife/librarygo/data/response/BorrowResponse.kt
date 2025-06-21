package com.clife.librarygo.data.response

import com.clife.librarygo.entity.Book

sealed class BorrowResponse {
    //查询借阅的图书信息通过用户ID
    data class SearchBorrowByIdResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val books: List<Book.BookBorrowed>? = null
    )

    //查询借阅的图书信息通过用户ID和书籍UID
    data class SearchBorrowByUserIdAndBookUidResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )

    //借阅图书
    data class BorrowBookResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )

    //归还图书
    data class DeleteBorrowRecordResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )
}
