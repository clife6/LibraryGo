package com.clife.librarygo.data

import com.clife.librarygo.data.retrofit.BookService
import com.clife.librarygo.data.retrofit.BorrowService
import com.clife.librarygo.data.retrofit.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.10.16.12:5050/"


    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // UserService实例
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    // BookService实例
     val bookService: BookService by lazy {
         retrofit.create(BookService::class.java)
     }

    // BorrowService实例
    val borrowService: BorrowService by lazy {
        retrofit.create(BorrowService::class.java)
    }
}