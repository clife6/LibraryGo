package com.clife.librarygo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clife.librarygo.R
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginCompose(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(),
) {
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }

    val loginError by viewModel.loginError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route) {
                // 例如，登录后不希望用户返回到登录页
                popUpTo(Routes.LOGIN) { inclusive = true } // 假设 Routes.LOGIN 是登录页路由
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF2575FC)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp) // 水平方向 padding
                .fillMaxWidth() // 让 Column 占据允许的最大宽度
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp), // 内边距
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo or App Icon
            Image(
                painter = painterResource(id = R.drawable.logo), // 使用导入的 R
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "图书借阅管理系统",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2575FC)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = { newValue ->
                    username.value = newValue
                    if (loginError != null) viewModel.clearLoginError() // 用户输入时清除错误
                },
                label = { Text("用户名") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = "Username Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2575FC),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2575FC),
                    cursorColor = Color(0xFF2575FC)
                ),
                isError = loginError != null // 如果有错误信息，标记输入框为错误状态
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Username Field
            OutlinedTextField(
                value = password.value,
                onValueChange = { newValue ->
                    password.value = newValue
                    if (loginError != null) viewModel.clearLoginError() // 用户输入时清除错误
                },
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2575FC),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2575FC),
                    cursorColor = Color(0xFF2575FC)
                ),
                isError = loginError != null // 如果有错误信息，标记输入框为错误状态
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { /* Handle login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2575FC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "登录", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "没有账号? 立即注册",
                    color = Color(0xFF2575FC),
                    fontSize = 14.sp,
                )
                Text(
                    text = "忘记密码?",
                    color = Color(0xFF2575FC),
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "管理员登录",
                color = Color(0xFF2575FC),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewLoginCompose() {
    LibraryGoTheme {
        LoginCompose(navController = rememberNavController())
    }
}