package com.clife.librarygo.entity

sealed class Book {
    // 书架图书信息
    data class BookInBookshelf(
        val uid: Long, //uid
        val title: String, //书名
        val author: String, //作者
        val work_image_url: String, //封面图片地址
        val publication_year: String, //出版年份
        val publisher : String, //出版社
    )

    // 借阅信息
    data class BookBorrowed(
        val id: Long, //id
        val book_uid: String, //书籍Uid
        val user_id: Long, //用户ID
        val borrow_date: String, //借阅时间
        val return_date: String, //归还时间
    )

    // 搜索结果中的书籍信息
    data class BookInSearch(
        val uid: Long, //uid
        val title: String, //书名
        val author: String, //作者
        val coverUrl: String, //封面图片地址
    )

    // 书籍详情
    data class BookDetail(
        val uid: Long, //uid
        val title: String, //书名
        val author: String, //作者
        val coverUrl: String, //封面图片地址
        val publisher: String, //出版社
        val productionCompany: String, //出品方
        val publicationYear: String, //出版年份
        val pages: Int, //页数
        val price: String, //定价
        val binding: String, //装帧
        val series: String, //丛书
        val isbn: String, //ISBN
        val contentSummary: String, //内容简介
        val authorSummary: String, //作者简介
    )
}