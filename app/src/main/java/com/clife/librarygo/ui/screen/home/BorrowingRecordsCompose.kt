package com.clife.librarygo.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.clife.librarygo.entity.BorrowRecord
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BorrowingRecordsViewModel


@Composable
fun BorrowingRecordsCompose(
    navController: NavController,
    id: String,
    viewModel: BorrowingRecordsViewModel = viewModel(),
) {
    val records = viewModel.records.collectAsState().value.reversed()
    // 加载借阅记录
    LaunchedEffect(id) {
        viewModel.loadBorrowingRecords(userId = id)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "借阅记录",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (records.isEmpty()) {
            Text(
                "没有借阅记录",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn {
                items(records.size) { index ->
                    BorrowRecordItem(record = records[index], navController = navController)
                    if (index < records.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp)) // 分隔符
                    }
                }
            }
        }
    }
}

@Composable
fun BorrowRecordItem(
    record: BorrowRecord.BorrowingRecord,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController.navigate(
                Routes.bookDetailWithIdAndUid(
                    id = record.userId,
                    uid = record.bookUid
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 用户和书籍信息
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "用户ID: ${record.userId}",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "书籍ID: ${record.bookUid}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 时间信息
            Text("借阅时间: ${record.borrowDate}")
            Text(
                "应还时间: ${record.returnDate}",
                color = if (record.status == "已逾期") Color.Red else Color.Unspecified
            )

            if (record.dueDate != null) {
                Text("实际归还时间: ${record.dueDate}")
            }

            // 状态
            Text(
                text = if (record.dueDate != null) "已归还"
                else if (record.status == "已逾期") "已逾期"
                else "借阅中",
                color = when {
                    record.dueDate != null -> Color.Green
                    record.status == "已逾期" -> Color.Red
                    else -> Color.Blue
                }
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewWelcomeCompose() {
    LibraryGoTheme {
        BorrowingRecordsCompose(
            id = "1234567890",
            navController = NavController(context = androidx.compose.ui.platform.LocalContext.current)
        )
    }
}