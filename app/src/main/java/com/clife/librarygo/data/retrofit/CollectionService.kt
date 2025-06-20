package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.request.CollectionRequest
import com.clife.librarygo.data.response.CollectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CollectionService {

    @GET("/collection/searchCollectionByUserIdAndBookUid")
    suspend fun searchCollectionByUserIdAndBookUid(
        @Query("user_id") userId: String,
        @Query("book_uid") bookUid: String,
    ): Response<CollectionResponse.SearchCollectionByUserIdAndBookUidResponse>

    @POST("/collection/updateCollection")
    suspend fun updateCollection(
        @Body request: CollectionRequest.UpdateCollectionRequest
    ): Response<CollectionResponse.UpdateCollectionResponse>
}