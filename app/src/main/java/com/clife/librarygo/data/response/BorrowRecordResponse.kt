package com.clife.librarygo.data.response

import com.clife.librarygo.entity.BorrowRecord

sealed class BorrowRecordResponse {

    data class AddBorrowRecordResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null
    )

    data class UpdateBorrowRecordResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null
    )

    data class SearchBorrowRecordResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val borrowRecord: BorrowRecord.SearchBorrowRecord? = null
    )
}