package com.clife.librarygo.data.request

sealed class CollectionRequest {

    data class UpdateCollectionRequest(
        val userId: String,
        val bookUid: String,
        val isCollection: Boolean
    )

    data class GetCollectionByUserIdRequest(
        val userId: String
    )
}