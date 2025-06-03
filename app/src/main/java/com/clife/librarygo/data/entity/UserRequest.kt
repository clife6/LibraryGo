package com.clife.librarygo.data.entity


sealed class UserRequest {

    data class SetNewPasswordRequest(
        val id: String,
        val newPassword: String
    )

}