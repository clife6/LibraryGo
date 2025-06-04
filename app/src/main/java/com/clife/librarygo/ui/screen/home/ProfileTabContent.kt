package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileTabHeader(
    id: String,
) {
    Text(
        text = "个人中心",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun ProfileTabContent(
    paddingValues: PaddingValues,
    id: String,
) {
    Text(
        text = "个人中心内容",
        modifier = Modifier.padding(paddingValues),
        style = MaterialTheme.typography.bodyLarge
    )
}