package com.clife.librarygo.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompose(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
) {
    val items = listOf("首页", "发现", "书架", "我的")
    val icons =
        listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Book, Icons.Filled.Person)
    val selectedTabIndex = viewModel.selectedTabIndex.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    when (selectedTabIndex) {
                        0 -> {
                            Text("首页", style = MaterialTheme.typography.titleMedium)
                        }
                        1 -> {
                            Text("图书发现", style = MaterialTheme.typography.titleMedium)
                        }
                        2 -> {
                            Text("我的书架", style = MaterialTheme.typography.titleMedium)
                        }
                        3 -> {
                            Text("个人中心", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.selectTab(index) }
                    )
                }
            }
        }

    ) { contentPadding ->
        // 根据选中的标签显示不同的内容
        when (selectedTabIndex) {
            0 -> HomeTabContent(contentPadding)
            1 -> DiscoverTabContent(contentPadding)
            2 -> BookshelfTabContent(contentPadding)
            3 -> ProfileTabContent(contentPadding)
        }
    }
}


@Composable
fun HomeTabContent(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    Text(
        text = "首页内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun DiscoverTabContent(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    Text(
        text = "发现页内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun BookshelfTabContent(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    Text(
        text = "我的书架内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ProfileTabContent(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    Text(
        text = "个人中心内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewHomeCompose() {
    LibraryGoTheme {
        HomeCompose(navController = rememberNavController())
    }
}