package com.clife.librarygo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clife.librarygo.data.local.dao.UserDao
import com.clife.librarygo.data.local.entity.UserEntity

//Room框架的核心注解，将类标记为数据库
@Database(
    entities = [UserEntity::class], //定义数据库包含的表实体
    version = 1 //数据库版本号，用于迁移管
)
abstract class AppDatabase : RoomDatabase() {   //定义为抽象类，继承自Room的RoomDatabase基类
    companion object {
        private val name = "AppDatabase.db" //数据库名称常量
        private var db: AppDatabase? = null //数据库实例，使用可空类型以便延迟初始化

        fun getInstance(context: Context): AppDatabase { //获取数据库实例的静态方法
            if (db == null) db = Room.databaseBuilder(
                context, //上下文对象，通常是Application或Activity
                AppDatabase::class.java, //数据库类的类型
                name //数据库名称
            ).enableMultiInstanceInvalidation()
                .build()
                .apply { db = this } //将构建的实例赋值给静态变量db
            return db!! //返回数据库实例，使用非空断言操作符
        }
    }

    abstract fun getUserDao(): UserDao //抽象方法，用于获取UserDao的实例（Room会在编译时自动实现）
}