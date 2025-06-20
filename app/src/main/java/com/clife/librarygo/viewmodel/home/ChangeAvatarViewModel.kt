package com.clife.librarygo.viewmodel.home

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ChangeAvatarViewModel : ViewModel(){
    val imageUris = mutableStateListOf<Uri>()

    var selectedAvatarUri by mutableStateOf<Uri?>(null)
        private set // 只能在ViewModel内部修改

    //从设备的相册加载图片
    fun loadImages(context: Context) {
        imageUris.clear()
        //projection指定查询的列，这里只查询图片的ID
        val projection = arrayOf(android.provider.MediaStore.Images.Media._ID)
        //sortOrder指定查询结果的排序方式，这里按添加时间降序排列
        val sortOrder = "${android.provider.MediaStore.Images.Media.DATE_ADDED} DESC"
        //使用ContentResolver查询外部存储的图片
        val query = context.contentResolver.query(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString()
                )
                imageUris.add(contentUri)
            }
        }
    }
    fun selectAvatar(uri: Uri) {
        selectedAvatarUri = uri
    }
}