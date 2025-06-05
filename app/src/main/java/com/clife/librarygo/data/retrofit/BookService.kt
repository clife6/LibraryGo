package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.response.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    // 查询图书信息通过书名或作者名
    @GET("/book/searchBookByTitleOrAuthor")
    suspend fun searchBookByTitleOrAuthor(
        @Query("title") title: String,
        @Query("author") author: String,
    ): Response<BookResponse.SearchBookResponse>

}