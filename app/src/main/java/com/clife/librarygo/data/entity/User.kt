package com.clife.librarygo.data.entity

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    val phone: String,
    val avatar: String,
    val role: String,
    val createdAt: String,
)