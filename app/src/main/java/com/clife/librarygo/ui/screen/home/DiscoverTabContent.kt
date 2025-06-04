package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DiscoverTabHeader(
    id: String,
) {
    Text(
        text = "图书发现",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun DiscoverTabContent(
    paddingValues: PaddingValues,
    id: String,
) {
    Text(
        text = "发现页内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}