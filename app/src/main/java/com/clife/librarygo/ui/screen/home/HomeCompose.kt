package com.clife.librarygo.ui.screen.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompose(
    navController: NavController,
    id: String,
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
                        0 -> HomeTabHeader(id)
                        1 -> DiscoverTabHeader(id)
                        2 -> BookshelfTabHeader(id)
                        3 -> ProfileTabHeader(id)
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
            0 -> HomeTabContent(contentPadding,id)
            1 -> DiscoverTabContent(contentPadding,id)
            2 -> BookshelfTabContent(contentPadding,id)
            3 -> ProfileTabContent(contentPadding,id)
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewHomeCompose() {
    LibraryGoTheme {
        HomeCompose(navController = rememberNavController(), "1")
    }
}