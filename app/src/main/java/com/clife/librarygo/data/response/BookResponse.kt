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
}