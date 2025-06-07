package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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
import com.clife.librarygo.viewmodel.home.HomeTabContentViewModel

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
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        val names = listOf("阅读推广", "图书馆大数据", "新闻咨询")
        var selected by remember { mutableIntStateOf(0) }
        LazyRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(names) { index, name ->
                Column(
                    modifier = Modifier
                        .padding(12.dp, 4.dp)
                        .width(IntrinsicSize.Max)
                ) {
                    Text(
                        name,
                        fontSize = 15.sp,
                        color = if (index == selected) Color(0xfffa9e51) else Color(0xffb4b4b4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == selected) Color(0xfffa9e51).copy(alpha = 0.1f) else Color.Transparent)
                            .padding(8.dp)
                            .clickable {
                                selected = index
                            }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(if (index == selected) Color(0xfffa9e51) else Color.Transparent)
                    )
                }
            }
        }
        when (selected) {
            0 -> {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "文学",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    LazyRow {
                        items(10) {
                            Card(
                                modifier = Modifier.size(150.dp, 250.dp)
                                    .padding(horizontal = 8.dp),
                                onClick = { },
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(2f / 3f)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data("https://img1.doubanio.com/view/subject/s/public/s27914268.jpg") // Replace with actual image URL
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "book.title",
                                            modifier = Modifier.fillMaxSize(),
                                            placeholder = ColorPainter(Color(0xFFF5F5F5)),
                                            error = rememberVectorPainter(Icons.Default.Book),
                                            contentScale = ContentScale.FillWidth
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "book.title",
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "book.author",
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

            1 -> {
                Text(
                    text = "图书馆内容2",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            2 -> {
                Text(
                    text = "图书馆内容3",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
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

