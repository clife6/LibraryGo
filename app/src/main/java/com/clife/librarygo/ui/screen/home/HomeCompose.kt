package com.clife.librarygo.ui.screen.home

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.service.DueCheckService
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

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val serviceIntent = Intent(context, DueCheckService::class.java).apply {
            putExtra("USER_ID", id)
        }
        context.startService(serviceIntent)
    }

    Scaffold(
        topBar = {
            if (selectedTabIndex != 1) {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.shadow(16.dp),
                    title = {
                        when (selectedTabIndex) {
                            0 -> HomeTabHeader(id)
                            2 -> BookshelfTabHeader(id)
                            3 -> ProfileTabHeader(id)
                        }
                    },
                )
            }
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
            0 -> HomeTabContent(
                paddingValues=contentPadding,
                navController=navController,
                id=id
            )
            1 -> DiscoverTabContent(
                paddingValues=contentPadding,
                navController=navController,
                id=id
            )
            2 -> BookshelfTabContent(
                paddingValues=contentPadding,
                navController=navController,
                id=id
            )
            3 -> ProfileTabContent(
                paddingValues=contentPadding,
                navController=navController,
                id=id,
                home = viewModel
            )
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