package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.response.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    // 查询图书信息通过书名或作者名
    @GET("/book/searchBookByTitleOrAuthor")
    suspend fun searchBookByTitleOrAuthor(
        @Query("keyword") keyword: String,
    ): Response<BookResponse.SearchBookResponse>

    // 查询图书信息通过书籍Uid
    @GET("/book/searchBookByUid")
    suspend fun searchBookByUid(
        @Query("uid") uid: String,
    ): Response<BookResponse.SearchBookByUidResponse>

    // 查询图书详细信息通过Uid
    @GET("/book/searchBookDetailByUid")
    suspend fun searchBookDetailByUid(
        @Query("uid") uid: String,
    ): Response<BookResponse.SearchBookDetailByUidResponse>

    //随机获取图书信息
    @GET("/book/getRandomBooks")
    suspend fun getRandomBooks(
        @Query("count") count: Int = 8, // 默认获取10本随机图书
    ): Response<BookResponse.RandomBooksResponse>
}