package com.clife.librarygo.data.response

sealed class CollectionResponse {

    data class SearchCollectionByUserIdAndBookUidResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )

    data class UpdateCollectionResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )
}