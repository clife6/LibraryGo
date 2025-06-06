package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.response.BorrowResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BorrowService {
    // 查询借阅的图书信息通过用户ID
    @GET("/borrow/searchBorrow")
    suspend fun searchBorrow(
        @Query("user_id") userId: String,
    ): Response<BorrowResponse.SearchBorrowByIdResponse>
}