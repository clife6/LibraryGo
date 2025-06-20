package com.clife.librarygo.entity

sealed class User {
    data class UserInfo(
        val id: Long,
        val username: String,
        val email: String,
        val phone: String,
        val avatar: String,
    )
}