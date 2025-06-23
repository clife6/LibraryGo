package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.clife.librarygo.R
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.HomeTabContentViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeTabHeader(
    id: String,
) {
    Text(
        text = "欢迎来到图书馆",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun HomeTabContent(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: HomeTabContentViewModel = viewModel(),
    id: String,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        // 轮播图部分
        item {
            val swiperList = remember {
                listOf(
                    R.drawable.s1,
                    R.drawable.s2,
                    R.drawable.s3,
                    R.drawable.s4,
                    R.drawable.s5,
                )
            }
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { swiperList.size }
            )
            Box(modifier = Modifier.height(250.dp)) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentPadding = PaddingValues(
                        horizontal = 56.dp,
                        vertical = 16.dp
                    ),
                    beyondViewportPageCount = 3
                ) { page ->
                    Image(
                        painter = painterResource(swiperList[page]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val currentIndex = pagerState.currentPage
                                scaleX = if (currentIndex == page) 1f else 0.9f
                                scaleY = if (currentIndex == page) 1f else 0.9f
                            }
                            .clip(RoundedCornerShape(18.dp))
                    )
                }
                CustomPagerIndicator(
                    pageCount = swiperList.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
            LaunchedEffect(key1 = pagerState) {
                while (true) {
                    delay(3000) // 3秒切换一次
                    val nextPage = (pagerState.currentPage + 1) % swiperList.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        // 新闻部分
        item {
            val newsList = listOf("最新动态", "校内公告")
            var newsSelected = remember { mutableIntStateOf(0) }
            LazyRow(
                Modifier.padding(0.dp, 8.dp),
                contentPadding = PaddingValues(12.dp, 0.dp),
            ) {
                itemsIndexed(newsList) { index, item ->
                    Column(
                        Modifier
                            .padding(12.dp, 4.dp)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text(
                            item, fontSize = 15.sp,
                            color = if (index == newsSelected.intValue) Color(0xfffa9e51) else Color(
                                0xffb4b4b4
                            ),
                            modifier = Modifier.clickable { newsSelected.intValue = index }
                        )
                    }
                }
            }
            when (newsSelected.intValue) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFD9F0FA))
                            .height(200.dp),
                    ) {
                        Text(
                            text = "‘不朽的印记——抗战文献主题展’在省身图书馆二楼大厅开展",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "嘉兴大学图书馆学生管理委员会成员招聘补充通知",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "军民共话智能潮： 嘉兴大学与驻嘉部队联合举办读书交流活动",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "关于嘉兴大学图书馆学生管理委员会成员招聘的通知",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "嘉兴大学举办‘太极拳锻炼解析’专题讲座 弘扬中华武术文化",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "图书馆关于2025年‘五一’假期开馆安排的通知",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "图书馆举办‘你选书·我买单’暨数据库资源推广双主题服务活动",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFD9F0FA))
                            .height(200.dp),
                    ) {
                        // 校内公告内容
                    }
                }
            }
        }

        // 阅读推广部分
        item {
            val readList = listOf("阅读推广")
            var readSelected = remember { mutableIntStateOf(0) }
            val books = viewModel.books.collectAsState().value
            LazyRow(
                Modifier.padding(0.dp, 8.dp),
                contentPadding = PaddingValues(12.dp, 0.dp)
            ) {
                itemsIndexed(readList) { index, item ->
                    Column(
                        Modifier
                            .padding(12.dp, 4.dp)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text(
                            item, fontSize = 15.sp,
                            color = if (index == readSelected.intValue) Color(0xfffa9e51) else Color(
                                0xffb4b4b4
                            ),
                            modifier = Modifier.clickable { readSelected.intValue = index }
                        )
                    }
                }
            }

            LaunchedEffect(Unit) {
                if (books.isEmpty()) {
                    viewModel.getBooks()
                }
            }

            when (readSelected.intValue) {
                0 -> {
                    if (books.isEmpty()) {
                        Text(
                            text = "暂无推荐书籍",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(710.dp),
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(books.size) { index ->
                                val book = books[index]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(
                                                Routes.bookDetailWithIdAndUid(
                                                    id,
                                                    book.uid.toString()
                                                )
                                            ) {
                                                launchSingleTop = true
                                            }
                                        },
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(2f / 3f)
                                        ) {
                                            AsyncImage(
                                                model = book.coverUrl,
                                                contentDescription = book.title,
                                                modifier = Modifier.fillMaxSize(),
                                                placeholder = ColorPainter(Color(0xFFF5F5F5)),
                                                error = rememberVectorPainter(Icons.Default.Book),
                                                contentScale = ContentScale.FillWidth
                                            )
                                        }
                                        Text(
                                            text = book.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = book.author,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CustomPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.LightGray,
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .background(
                        color = if (index == currentPage) activeColor else inactiveColor,
                        shape = CircleShape
                    )
                    .padding(spacing)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewHomeTabContent() {
    LibraryGoTheme {
        HomeCompose(navController = rememberNavController(), "1")
    }
}

