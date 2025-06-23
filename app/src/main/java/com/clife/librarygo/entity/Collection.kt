package com.clife.librarygo.entity

sealed class Collection {
    data class CollectionInfo(
        val id: Long,
        val userId: String,
        val bookUid: String,
    )
}