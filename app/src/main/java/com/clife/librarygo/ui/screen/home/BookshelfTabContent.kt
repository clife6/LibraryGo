package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    Text(
        text = "我的书架内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}