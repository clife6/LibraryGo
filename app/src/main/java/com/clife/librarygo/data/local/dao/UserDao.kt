package com.clife.librarygo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.clife.librarygo.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    //检查用户账号与密码是否匹配
    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE username = :username AND password = :password)")
    suspend fun checkCredentials(username: String, password: String): Boolean // 返回凭证是否匹配

    //检查注册用户名是否已存在
    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE username = :username)")
    suspend fun isUsernameExists(username: String): Boolean // 返回用户名是否已存在

    //插入新用户，返回是否成功
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long // 返回新用户的ID，如果插入失败则返回-1
}