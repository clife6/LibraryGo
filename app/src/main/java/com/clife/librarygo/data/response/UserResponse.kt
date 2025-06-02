package com.clife.librarygo.data.response

sealed class UserResponse {
    data class SearchUserResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )
}