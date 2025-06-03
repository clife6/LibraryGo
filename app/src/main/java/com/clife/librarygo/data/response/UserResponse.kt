package com.clife.librarygo.data.response

sealed class UserResponse {

    data class SearchUserResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )

    data class CheckUserEmailAndPhoneResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
        val id: Long? = null,
    )

    //设置新的密码的响应
    data class SetNewPasswordResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )

    //注册新用户的响应
    data class AddUserResponse(
        val code: Int,
        val message: String? = null,
        val error: String? = null,
    )
}