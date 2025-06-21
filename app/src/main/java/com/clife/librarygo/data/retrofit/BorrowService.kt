package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.request.BorrowRequest
import com.clife.librarygo.data.response.BorrowResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BorrowService {
    // 查询借阅的图书信息通过用户ID
    @GET("/borrow/searchBorrow")
    suspend fun searchBorrow(
        @Query("user_id") userId: String,
    ): Response<BorrowResponse.SearchBorrowByIdResponse>

    // 查询借阅的图书信息通过用户ID和书籍UID
    @GET("/borrow/searchBorrowByUserIdAndBookUid")
    suspend fun searchBorrowByUserIdAndBookUid(
        @Query("user_id") userId: String,
        @Query("book_uid") bookUid: String,
    ): Response<BorrowResponse.SearchBorrowByUserIdAndBookUidResponse>

    // 借阅图书
    @POST("/borrow/borrowBook")
    suspend fun borrowBook(
        @Body request: BorrowRequest.BorrowBookRequest
    ): Response<BorrowResponse.BorrowBookResponse>

    // 归还图书
    @POST("/borrow/deleteBorrowRecord")
    suspend fun deleteBorrowRecord(
        @Body request: BorrowRequest.DeleteBorrowRecordRequest
    ): Response<BorrowResponse.DeleteBorrowRecordResponse>
}