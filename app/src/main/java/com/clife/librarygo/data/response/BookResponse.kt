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

    //查询图书信息通过书籍Uid
    data class SearchBookByUidResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val book: Book.BookInBookshelf? = null
    )

    //查询图书详细信息通过Uid
    data class SearchBookDetailByUidResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val book: Book.BookDetail? = null
    )
}