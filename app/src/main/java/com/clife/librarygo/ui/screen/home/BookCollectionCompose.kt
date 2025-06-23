package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BookCollectionViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale

@Composable
fun BookCollectionCompose(
    navController: NavController,
    id: String,
    viewModel: BookCollectionViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            BookCollectionTopBar(
            )
        },
        content = { paddingValues ->
            BookCollectionContent(
                navController = navController,
                paddingValues = paddingValues,
                id = id,
                viewModel = viewModel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCollectionTopBar(
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                text = "我的收藏",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
fun BookCollectionContent(
    navController: NavController,
    paddingValues: PaddingValues,
    id: String,
    viewModel: BookCollectionViewModel
) {
    val books by viewModel.books.collectAsState()
    val count by viewModel.count.collectAsState()
    val bookErrorMessage by viewModel.bookErrorMessage.collectAsState()
    val collectionErrorMessage by viewModel.collectionErrorMessage.collectAsState()

    LaunchedEffect(id) {
        viewModel.searchCollectionByUserId(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (count != -1) {
            Text(
                text = "共${count}本收藏",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            )
        }
        if (bookErrorMessage != null) {
            Text(
                text = bookErrorMessage ?: "",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            )
        }
        if(collectionErrorMessage != null) {
            Text(
                text = collectionErrorMessage ?: "",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(books) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            com.clife.librarygo.navigation.Routes.bookDetailWithIdAndUid(
                                id,
                                book.uid.toString()
                            )
                        )
                    },
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
                                model = book.work_image_url,
                                contentDescription = book.title,
                                modifier = Modifier.fillMaxSize(),
                                placeholder = ColorPainter(Color(0xFFF5F5F5)),
                                error = rememberVectorPainter(Icons.Default.Book),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
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
                        Text(
                            text = "出版社：${book.publisher}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "出版年份：${book.publication_year}",
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

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun PreviewBookCollectionCompose() {
    LibraryGoTheme {
        BookCollectionCompose(
            navController = rememberNavController(),
            id = "1",
        )
    }
}

