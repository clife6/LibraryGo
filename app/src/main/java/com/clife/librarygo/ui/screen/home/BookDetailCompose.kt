package com.clife.librarygo.ui.screen.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
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
    val isLoading by viewModel.isLoading.collectAsState()
    val bookDetailError by viewModel.bookDetailError.collectAsState()

    class DescriptionState {
        var contentExpanded by mutableStateOf(false)
        var authorExpanded by mutableStateOf(false)
    }

    var isFavorite by remember { mutableStateOf(false) }
    var isBorrowed by remember { mutableStateOf(false) }
    var descriptionState = remember { DescriptionState() }
    var backEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        viewModel.setBookDetail(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (backEnabled) {
                                backEnabled = false
                                navController.popBackStack()
                            }
                        },
                        enabled = backEnabled
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        enabled = !isLoading && bookDetail != null
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "收藏",
                            tint = if (isFavorite) Color(0xFFFFD700) else Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isBorrowed = !isBorrowed },
                        enabled = !isLoading && bookDetail != null
                    ) {
                        Text(if (isBorrowed) "去还书" else "去借阅")
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
                if (bookDetailError != null) {
                    Text(
                        text = bookDetailError ?: "",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                DescriptionItem(
                    title = "内容简介",
                    content = (if (bookDetail?.contentSummary.isNullOrEmpty()) "暂无信息" else bookDetail?.contentSummary).toString(),
                    expanded = descriptionState.contentExpanded,
                    onExpandClick = {
                        descriptionState.contentExpanded = !descriptionState.contentExpanded
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                DescriptionItem(
                    title = "作者简介",
                    content = (if (bookDetail?.authorSummary.isNullOrEmpty()) "暂无信息" else bookDetail?.authorSummary).toString(),
                    expanded = descriptionState.authorExpanded,
                    onExpandClick = {
                        descriptionState.authorExpanded = !descriptionState.authorExpanded
                    }
                )
            }
            val bookInfo = listOf(
                "出版社" to bookDetail?.publisher,
                "定价" to bookDetail?.price,
                "页数" to (bookDetail?.pages?.toString() ?: ""),
                "出版年" to bookDetail?.publicationYear,
                "装帧" to bookDetail?.binding,
                "出品方" to bookDetail?.productionCompany,
                "丛书" to bookDetail?.series,
                "ISBN" to bookDetail?.isbn
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "书籍信息",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    bookInfo.forEach { (label, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (value.isNullOrEmpty()) "暂无信息" else value,
                                fontSize = 15.sp,
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
}

@Composable
fun DescriptionItem(
    title: String,
    content: String,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxLines = if (expanded) Int.MAX_VALUE else 5
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        BoxWithConstraints(
            modifier = Modifier.padding(16.dp)
        ) {
            val widthPx =
                with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx().toInt() }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onExpandClick() }
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "收起" else "展开",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.lineCount(15.sp, LocalDensity.current, widthPx) > 5) {
                    TextButton(
                        onClick = onExpandClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = if (expanded) "收起" else "查看更多",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

fun String.lineCount(
    textSize: TextUnit,
    density: Density,
    widthPx: Int
): Int {
    val paint = android.text.TextPaint().apply {
        this.textSize = with(density) { textSize.toPx() }
    }
    val layout = android.text.StaticLayout.Builder
        .obtain(this, 0, length, paint, widthPx)
        .build()
    return layout.lineCount
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