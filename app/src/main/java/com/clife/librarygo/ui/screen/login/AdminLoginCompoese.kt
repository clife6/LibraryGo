package com.clife.librarygo.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.clife.librarygo.viewmodel.login.AdminLoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AdminLoginCompose(
    navController: NavController,
    viewModel: AdminLoginViewModel = viewModel(),
){
    val adminName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }//控制密码是否显示为明文

    val loginError by viewModel.loginError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route) {
                popUpTo(Routes.LOGIN_ADMIN) { inclusive = true }
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
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp) // 水平方向 padding
                .fillMaxWidth() // 让 Column 占据允许的最大宽度
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp), // 内边距
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "管理员登录",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2575FC)
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = adminName.value,
                onValueChange = { newValue ->
                    adminName.value = newValue
                    if (loginError != null) viewModel.clearLoginError()
                },
                label = { Text("管理员") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.AdminPanelSettings, contentDescription = "AdminName Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2575FC),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2575FC),
                    cursorColor = Color(0xFF2575FC)
                ),
                isError = loginError != null
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password.value,//文本字段的当前值
                onValueChange = { newValue ->
                    password.value = newValue
                    if (loginError != null) viewModel.clearLoginError()
                },//当值改变时的回调
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,//是否单行文本
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "切换密码可见性",
                        modifier = Modifier.clickable { passwordVisible.value = !passwordVisible.value }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2575FC),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2575FC),
                    cursorColor = Color(0xFF2575FC)
                ),
                isError = loginError != null
            )
            if (loginError != null) {
                Text(
                    text = loginError ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.login(adminName.value, password.value) },
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
            Text(
                text = "用户登录",
                color = Color(0xFF2575FC),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.End)
                    .clickable {
                        // 跳转到用户登录页面
                        navController.navigate(Routes.LOGIN)
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
fun PreviewAdminLoginCompose() {
    LibraryGoTheme {
        AdminLoginCompose(navController = rememberNavController())
    }
}