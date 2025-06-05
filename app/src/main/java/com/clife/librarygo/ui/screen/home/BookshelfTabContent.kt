package com.clife.librarygo.ui.screen.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BookshelfTabContentViewModel

@Composable
fun BookshelfTabHeader(
    id: String,
) {
    Text(
        text = "我的书架",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun BookshelfTabContent(
    paddingValues: PaddingValues,
    id: String,
    viewModel: BookshelfTabContentViewModel = viewModel()
) {
    val books by viewModel.books.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadBookshelf(id)
    }

    // 弹窗展示错误
    if (!errorMessage.isNullOrEmpty()) {
        val (titleText, contentText) = when (errorMessage) {
            "暂无图书" -> "书架是空的哦" to "快去借阅一些图书吧！"
            "网络错误" -> "网络错误" to "请检查您的网络连接"
            else -> "错误" to (errorMessage ?: "")
        }
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text(titleText) },
            text = { Text(contentText) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) {
                    Text("确定")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.padding(paddingValues),
    ) {
        for (book in books) {
            item(key = book.uid) {
                //展示book内容
                Card(
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 3.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color(0xFFFAF8FF)),
                    onClick = { /* 点击事件 */ }
                ) {
                    Row{
                        AsyncImage(
                            model = book.coverUrl,
                            contentDescription = "Book Cover",
                            modifier = Modifier
                                .padding(start = 4.dp, top = 9.dp, bottom = 9.dp)
                                .size(120.dp) // 可选，设置图片大小
                        )
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF3A86B6)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "作者: ${book.author}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF201616)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "作者: ${book.publisher}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewBookshelfTableContent() {
    LibraryGoTheme {
        HomeCompose(navController = rememberNavController(), "1")
    }
}