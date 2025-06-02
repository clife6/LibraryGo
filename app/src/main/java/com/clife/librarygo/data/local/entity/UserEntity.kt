package com.clife.librarygo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long, //用户ID
    @ColumnInfo(name = "username") val username: String, //用户名
    @ColumnInfo(name = "password") val password: String, //密码
    @ColumnInfo(name = "email") val email: String, //邮箱
    @ColumnInfo(name = "phone") val phone: String = "", //手机号
    @ColumnInfo(name = "role") val role: String, //角色（如管理员、普通用户等）
    @ColumnInfo(name = "created_at") val createdAt: Long, //注册时间戳
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null //用户头像
)