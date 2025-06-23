package com.clife.librarygo.data.response

import com.clife.librarygo.entity.Collection.CollectionInfo

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

    data class GetCollectionByUserIdResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val collectionList: List<CollectionInfo>? = null
    )
}