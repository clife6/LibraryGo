package com.clife.librarygo.data.request


sealed class UserRequest {

    data class SetNewPasswordRequest(
        val id: String,
        val newPassword: String
    )
    
    data class AddUserRequest(
        val username: String,
        val password: String,
        val email: String,
        val phone: String
    )

    data class LoginAdminRequest(
        val adminName: String,
        val password: String
    )

    data class LoginUserRequest(
        val username: String,
        val password: String
    )

    data class SetNewAvatarRequest(
        val userId: String,
        val avatar: String
    )
}