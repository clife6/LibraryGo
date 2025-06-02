package com.clife.librarygo.data.retrofit

import com.clife.librarygo.data.response.UserResponse.SearchUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    //查询用户名是否已存在
    @GET("/user/searchUser")
    suspend fun searchUser(@Query("username") username: String): Response<SearchUserResponse>
}