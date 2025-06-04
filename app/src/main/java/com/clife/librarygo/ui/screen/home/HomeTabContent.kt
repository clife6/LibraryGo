package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    id: String,
) {
    Text(
        text = "首页内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}

