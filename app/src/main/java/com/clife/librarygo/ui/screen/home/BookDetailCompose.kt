package com.clife.librarygo.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BookDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailCompose(
    navController: NavController,
    id: String,
    uid: String,
    viewModel: BookDetailViewModel = viewModel(),
) {
    val bookDetail by viewModel.bookDetail.collectAsState()

    LaunchedEffect(uid) {
        viewModel.setBookDetail(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {}) {
                        Text("加入书架")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {}) {
                        Text("去借阅")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bookDetail?.coverUrl)
                        .crossfade(true)
                        .listener(
                            onError = { _, throwable ->
                                Log.e(
                                    "Coil",
                                    "加载失败: ${throwable.throwable.message}"
                                )
                            }
                        )
                        .build(),
                    contentDescription = "书籍封面",
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(2f / 3f),
                    error = rememberVectorPainter(Icons.Default.Book),
                    contentScale = ContentScale.FillWidth
                )
            }
            Text(
                text = "《${if (bookDetail?.title.isNullOrEmpty()) "未知书名" else bookDetail?.title}》",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = (if (bookDetail?.author.isNullOrEmpty()) "未知作者" else bookDetail?.author).toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            val bookInfo = listOf(
                "出版社" to bookDetail?.publisher,
                "出品方" to bookDetail?.productionCompany,
                "出版年" to bookDetail?.publicationYear,
                "页数" to (bookDetail?.pages?.toString() ?: ""),
                "定价" to bookDetail?.price,
                "装帧" to bookDetail?.binding,
                "丛书" to bookDetail?.series,
                "ISBN" to bookDetail?.isbn
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                bookInfo.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = if (value.isNullOrEmpty()) "未找到相关信息" else value,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun PreviewBookDetailCompose() {
    LibraryGoTheme {
        BookDetailCompose(
            navController = rememberNavController(),
            "1",
            "1",
        )
    }
}