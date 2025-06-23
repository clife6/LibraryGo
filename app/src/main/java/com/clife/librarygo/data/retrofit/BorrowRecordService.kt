package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.request.BorrowRecordRequest
import com.clife.librarygo.data.response.BorrowRecordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BorrowRecordService {

    // 添加一条借阅记录
    @POST("/borrowRecord/addBorrowRecord")
    suspend fun addBorrowRecord(
        @Body request: BorrowRecordRequest.AddBorrowRecordRequest
    ): Response<BorrowRecordResponse.AddBorrowRecordResponse>

    // 根据用户id和书籍uid更新借阅记录中的实际归还日期和状态
    @POST("/borrowRecord/updateBorrowRecord")
    suspend fun updateBorrowRecord(
        @Body request: BorrowRecordRequest.UpdateBorrowRecordRequest
    ): Response<BorrowRecordResponse.UpdateBorrowRecordResponse>

    // 根据用户id和书籍uid查询借阅记录
    @GET("/borrowRecord/searchBorrowRecord")
    suspend fun searchBorrowRecord(
        @Query("book_uid") bookUid: String,
        @Query("user_id") userId: String,
    ): Response<BorrowRecordResponse.SearchBorrowRecordResponse>

    // 根据用户id查询所有借阅记录
    @GET("/borrowRecord/searchBorrowRecordsByUserId")
    suspend fun searchBorrowRecordsByUserId(
        @Query("user_id") userId: String,
    ): Response<BorrowRecordResponse.BorrowingRecordsResponse>
}