package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.entity.UserRequest
import com.clife.librarygo.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    //查询用户名是否已存在
    @GET("/user/searchUser")
    suspend fun searchUser(@Query("username") username: String): Response<UserResponse.SearchUserResponse>

    //查询用户名的邮箱与手机号是否一致如果一致需要返回该账号的id
    @GET("/user/checkUserEmailAndPhone")
    suspend fun checkUserEmailAndPhone(
        @Query("email") email: String,
        @Query("phone") phone: String
    ): Response<UserResponse.CheckUserEmailAndPhoneResponse>

    //设置新的密码
    @POST("/user/setNewPassword")
    suspend fun setNewPassword(
        @Body request: UserRequest.SetNewPasswordRequest
    ): Response<UserResponse.SetNewPasswordResponse>

    //注册新用户
    @POST("/user/addUser")
    suspend fun addUser(
        @Body request: UserRequest.AddUserRequest
    ): Response<UserResponse.AddUserResponse>
}