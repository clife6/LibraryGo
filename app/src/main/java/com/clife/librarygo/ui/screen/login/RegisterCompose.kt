package com.clife.librarygo.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.clife.librarygo.viewmodel.login.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterCompose(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val formState by viewModel.formState.collectAsState()

    // 监听按钮加载状态
    val isLoading by viewModel.isLoading.collectAsState()

    // 监听导航事件
    LaunchedEffect(Unit) {
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route) {
                popUpTo(Routes.REGISTER) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2575FC)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "用户注册",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2575FC)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 用户名
            OutlinedTextField(
                value = formState.username,
                onValueChange = viewModel::updateUsername,
                label = { Text("用户名") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "用户名图标") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (formState.usernameError) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (formState.usernameError) Color.Red else Color.LightGray,
                    focusedLabelColor = if (formState.usernameError) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (formState.usernameError) Color.Red else Color(0xFF2575FC)
                ),
                isError = formState.usernameError
            )
            if (formState.usernameError) {
                Text(
                    text = formState.usernameErrorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            // 邮箱
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formState.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("电子邮箱") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "邮箱图标") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (formState.emailError) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (formState.emailError) Color.Red else Color.LightGray,
                    focusedLabelColor = if (formState.emailError) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (formState.emailError) Color.Red else Color(0xFF2575FC)
                ),
                isError = formState.emailError
            )
            if (formState.emailError) {
                Text(
                    text = formState.emailErrorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            // 电话
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formState.phone,
                onValueChange = viewModel::updatePhone,
                label = { Text("电话号码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "电话图标") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (formState.phoneError) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (formState.phoneError) Color.Red else Color.LightGray,
                    focusedLabelColor = if (formState.phoneError) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (formState.phoneError) Color.Red else Color(0xFF2575FC)
                ),
                isError = formState.phoneError
            )
            if (formState.phoneError) {
                Text(
                    text = formState.phoneErrorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 密码
            OutlinedTextField(
                value = formState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (formState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "密码图标") },
                trailingIcon = {
                    Icon(
                        imageVector = if (formState.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "切换密码可见性",
                        modifier = Modifier.clickable { viewModel.togglePasswordVisibility() }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (formState.passwordError) Color.Red else Color.LightGray,
                    focusedLabelColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC)
                ),
                isError = formState.passwordError
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 确认密码
            OutlinedTextField(
                value = formState.confirmPassword,
                onValueChange = viewModel::updateConfirmPassword,
                label = { Text("确认密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (formState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "确认密码图标") },
                trailingIcon = {
                    Icon(
                        imageVector = if (formState.confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "切换密码可见性",
                        modifier = Modifier.clickable { viewModel.toggleConfirmPasswordVisibility() }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (formState.passwordError) Color.Red else Color.LightGray,
                    focusedLabelColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (formState.passwordError) Color.Red else Color(0xFF2575FC)
                ),
                isError = formState.passwordError
            )
            if (formState.passwordError) {
                Text(
                    text = formState.passwordErrorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            if (formState.formError) {
                Text(
                    text = "请检查您是否输入有误",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.register() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2575FC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "注册", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                // 左侧如果有错误信息则显示
                if (formState.registerError) {
                    Text(
                        text = formState.registerErrorMessage,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f)) //空白符，保证登陆链接在右侧
                // 在右侧添加返回登陆界面
                Text(
                    text = "已有账号? 立即登录",
                    color = Color(0xFF2575FC),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }.align(Alignment.CenterVertically)
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewRegisterCompose() {
    LibraryGoTheme {
        RegisterCompose(navController = rememberNavController())
    }
}