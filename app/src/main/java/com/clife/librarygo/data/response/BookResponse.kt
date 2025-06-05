package com.clife.librarygo.data.response

import com.clife.librarygo.entity.Book

sealed class BookResponse {
    data class SearchBookResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val count: Int? = null,
        val books: List<Book.BookInSearch>? = null,
    )

    //查询借阅的图书信息通过用户ID
    data class SearchBorrowByIdResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val books: List<Book.BookBorrowed>? = null
    )

    //查询图书信息通过书籍Uid
    data class SearchBookByUidResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val book: Book.BookInBookshelf? = null
    )
}