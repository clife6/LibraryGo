package com.clife.librarygo.entity

sealed class Book {
    data class BookInBookshelf(
        val uid: Long, //uid
        val title: String, //书名
        val author: String, //作者
        val coverUrl: String, //封面图片地址
        val description: String, //简介
//        val isRead: Boolean = false,
//        val isFavorite: Boolean = false,
    )

    data class BookInSearch(
        val uid: Long, //uid
        val title: String, //书名
        val author: String, //作者
        val coverUrl: String, //封面图片地址
    )
}