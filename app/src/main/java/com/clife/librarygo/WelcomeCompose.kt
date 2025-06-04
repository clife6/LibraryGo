package com.clife.librarygo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.ui.theme.LibraryGoTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun WelcomeCompose(
    navController: NavController,
    viewModel: WelcomeViewModel = viewModel(),
) {
    // 监听来自 ViewModel 的导航事件
    LaunchedEffect(Unit) { // key 为 Unit 表示此 LaunchedEffect 仅在 Composable 首次组合时运行
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route) {
                // 从返回堆栈中弹出 Welcome 屏幕
                popUpTo(Routes.WELCOME) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF2575FC)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(250.dp, 250.dp)
                .clip(CircleShape)
        )
        Text(
            text = "图书借阅管理系统",
            modifier = Modifier
                .padding(top = 16.dp)
                .then(Modifier),
            fontSize = 24.dp.value.sp
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewWelcomeCompose() {
    LibraryGoTheme {
        // Preview 时，导航逻辑不会实际执行，因为 NavController 是模拟的或未完全配置
        // 为了让 Preview 不报错，可以传递一个 rememberNavController()
        // 但实际的跳转行为需要在运行应用时验证
        WelcomeCompose(
            navController = rememberNavController() // 使用 rememberNavController() 以便 Preview 渲染
        )
    }
}