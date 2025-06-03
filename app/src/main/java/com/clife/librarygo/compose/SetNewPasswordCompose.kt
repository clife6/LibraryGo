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
import com.clife.librarygo.viewmodel.SetNewPasswordViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetNewPasswordCompose(
    navController: NavController,
    id: String,
    viewModel: SetNewPasswordViewModel = viewModel(),
){
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    // 可见性控制
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    val setNewPasswordError by viewModel.setNewPasswordError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateTo.collectLatest { route ->
            navController.navigate(route) {
                popUpTo(Routes.SET_NEW_PASSWORD) { inclusive = true }
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
                text = "重新设置新的密码",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2575FC)
            )
            Spacer(modifier = Modifier.height(32.dp))
            //密码输入框
            OutlinedTextField(
                value = newPassword.value,
                onValueChange = { newValue ->
                    newPassword.value = newValue
                    if (setNewPasswordError != null) viewModel.clearSetNewPasswordError()
                },
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
                isError = setNewPasswordError != null
            )
            Spacer(modifier = Modifier.height(16.dp))
            //确认密码输入框
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { newValue ->
                    confirmPassword.value = newValue
                    if (setNewPasswordError != null) viewModel.clearSetNewPasswordError()
                },
                label = { Text("确认密码")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,//是否单行文本
                visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "切换密码可见性",
                        modifier = Modifier.clickable { confirmPasswordVisible.value = !confirmPasswordVisible.value }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF2575FC),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2575FC),
                    cursorColor = Color(0xFF2575FC)
                ),
                isError = setNewPasswordError != null
            )
            if (setNewPasswordError != null) {
                Text(
                    text = setNewPasswordError ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.setNewPassword(id,newPassword.value,confirmPassword.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2575FC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "确认", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "返回上一步",
                color = Color(0xFF2575FC),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        // 跳转到用户验证邮箱与电话号码页面
                        navController.navigate(Routes.RETRIEVE_PASSWORD)
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
fun PreviewSetNewPasswordCompose() {
    LibraryGoTheme {
        SetNewPasswordCompose(navController = rememberNavController(),"1234")
    }
}