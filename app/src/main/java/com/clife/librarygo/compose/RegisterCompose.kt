package com.clife.librarygo.compose

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.clife.librarygo.viewmodel.RegisterViewModel

@Composable
fun RegisterCompose(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
){
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }

    var passwordVisible = remember { mutableStateOf(false) }//控制密码是否显示为明文

//    val loginError by viewModel.loginError.collectAsState()
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
            Image(painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "用户注册",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2575FC)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    password.value = it
                },//未完成
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },//未完成
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it

                },//未完成
                label = { Text("确认密码")},
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Routes.LOGIN_ADMIN) },//需实现账号创建逻辑
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
            Text(
                text = "已有账号?前去登录",
                color = Color(0xFF2575FC),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.End)
                    .clickable {
                        // 跳转到登录页面
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
fun PreviewRegisterCompose() {
    LibraryGoTheme {
        // Preview 时，导航逻辑不会实际执行，因为 NavController 是模拟的或未完全配置
        // 为了让 Preview 不报错，可以传递一个 rememberNavController()
        // 但实际的跳转行为需要在运行应用时验证
        RegisterCompose(
            navController = rememberNavController() // 使用 rememberNavController() 以便 Preview 渲染
        )
    }
}