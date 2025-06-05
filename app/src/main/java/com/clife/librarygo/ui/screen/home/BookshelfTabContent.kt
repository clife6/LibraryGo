package com.clife.librarygo.ui.screen.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.clife.librarygo.entity.Book
import com.clife.librarygo.ui.theme.LibraryGoTheme

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
) {
    val books = remember {
        listOf(
            Book.BookInBookshelf(1, "Android开发艺术探索", "任玉刚","https://example.com/cover1.jpg", "一本关于Android开发的经典书籍"),
            Book.BookInBookshelf(2, "Kotlin实战", "Dmitry","https://example.com/cover2.jpg", "一本关于Kotlin编程语言的实战指南"),
            Book.BookInBookshelf(3, "算法图解", "Aditya Bhargava","https://example.com/cover3.jpg", "一本通俗易懂的算法入门书籍"),
        )
    }

    LazyColumn(
        modifier = Modifier.padding(paddingValues),
    ) {
        for (book in books) {
            item(key = book.id) {
                //展示book内容
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row {
                        AsyncImage(
                            model = "https://img1.doubanio.com/view/subject/s/public/s35028280.jpg",
                            contentDescription = "Book Cover",
                        )
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "作者: ${book.author}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = book.description,
                                style = MaterialTheme.typography.bodySmall
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