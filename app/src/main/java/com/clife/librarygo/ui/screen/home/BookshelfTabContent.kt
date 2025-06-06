package com.clife.librarygo.ui.screen.home


import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BookshelfTabContentViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookshelfTabHeader(
    id: String,
) {
    Text(
        text = "我的书架",
        style = MaterialTheme.typography.headlineMedium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfTabContent(
    navController: NavController,
    paddingValues: PaddingValues,
    id: String,
    viewModel: BookshelfTabContentViewModel = viewModel()
) {
    val books by viewModel.books.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // 下拉刷新状态
    val isRefreshing = remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    // 用于防止频繁刷新
    val lastRefreshTime = remember { mutableLongStateOf(0L) }
    val showDebounceDialog = remember { mutableStateOf(false) }

    val onRefresh: () -> Unit = {
        val now = System.currentTimeMillis()
        if (now - lastRefreshTime.longValue < 500) { //0.5秒防抖
            showDebounceDialog.value = true
        }else{
            lastRefreshTime.longValue = now
            isRefreshing.value = true
            viewModel.pullToRefresh(id)
            isRefreshing.value = false
        }
    }

    // 监听图书信息变化
    LaunchedEffect(id) {
        viewModel.refreshBookshelf(id)
    }

    // 监听导航事件
    LaunchedEffect(Unit) {
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route)
        }
    }

    // 错误弹窗
    if (!errorMessage.isNullOrEmpty() && errorMessage != "暂无图书") {
        val (titleText, contentText) = when (errorMessage) {
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

    // 防抖弹窗
    if (showDebounceDialog.value) {
        AlertDialog(
            onDismissRequest = { showDebounceDialog.value = false },
            title = { Text("提示") },
            text = { Text("请不要频繁刷新\n刷新间隔为0.5s") },
            confirmButton = {
                Button(onClick = { showDebounceDialog.value = false }) {
                    Text("确定")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxWidth()){
        PullToRefreshBox(
            isRefreshing = isRefreshing.value,
            onRefresh = onRefresh,
            modifier = Modifier.padding(),
            state = state,
            indicator =  {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing.value,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            },
        ){
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                if (books.isEmpty()){
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp), // 保证有下拉区域
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无图书，试试下拉刷新",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF3A86B6)
                            )
                        }
                    }
                }else{
                    for (book in books) {
                        item(key = book.uid) {
                            //展示book内容
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp, vertical = 3.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(Color(0xFFFAF8FF)),
                                onClick = {
                                    navController.navigate(
                                        Routes.bookDetailWithIdAndUid(
                                            id,
                                            book.uid.toString()
                                        )
                                    )
                                }
                            ) {
                                Row{
                                    AsyncImage(
                                        model = book.work_image_url,
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
                                            text = "出版社: ${book.publisher}",
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