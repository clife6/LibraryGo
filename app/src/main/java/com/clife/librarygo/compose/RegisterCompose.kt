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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.clife.librarygo.viewmodel.RegisterViewModel



@Composable
fun RegisterCompose(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
){
    // 输入状态
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var phone = remember { mutableStateOf("") }

    // 可见性控制
    var passwordVisible = remember { mutableStateOf(false) }
    var confirmPasswordVisible = remember { mutableStateOf(false) }

    // 错误状态
    var passwordError = remember { mutableStateOf(false) }
    var passwordErrorMessage = remember { mutableStateOf("") }
    var emailError = remember { mutableStateOf(false) }
    var emailErrorMessage = remember { mutableStateOf("") }
    var phoneError = remember { mutableStateOf(false) }
    var phoneErrorMessage = remember { mutableStateOf("") }
    var formError = remember { mutableStateOf(false) }

    // 从ViewModel收集错误状态
    // val registerError by viewModel.registerError.collectAsState()
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
            //用户名输入框
            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    username.value = it
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
            //电子邮箱输入框
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    // 实时验证邮箱格式
                    validateEmail(email.value).let {
                        emailError.value = it.first
                        emailErrorMessage.value = it.second
                    }
                },//未完成
                label = { Text("电子邮箱") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = "Email Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (emailError.value) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (emailError.value) Color.Red else Color.LightGray,
                    focusedLabelColor = if (emailError.value) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (emailError.value) Color.Red else Color(0xFF2575FC)
                ),
                isError = emailError.value
            )
            //邮箱错误提示
            if (emailError.value) {
                Text(
                    text = emailErrorMessage.value,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            //电话号码输入框
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phone.value,
                onValueChange = {
                    // 限制只能输入数字
                    if (it.all { char -> char.isDigit() }) {
                        phone.value = it
                        // 实时验证电话号码格式
                        validatePhoneNumber(phone.value).let {
                            phoneError.value = it.first
                            phoneErrorMessage.value = it.second
                        }
                    }
                },
                label = { Text("电话号码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = "Phone Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (phoneError.value) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (phoneError.value) Color.Red else Color.LightGray,
                    focusedLabelColor = if (phoneError.value) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (phoneError.value) Color.Red else Color(0xFF2575FC)
                ),
                isError = phoneError.value
            )
            //电话号码错误提示
            if (phoneError.value) {
                Text(
                    text = phoneErrorMessage.value,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            //密码输入框
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    checkPasswordMatch(password.value, confirmPassword.value).let {
                        passwordError.value = it.first
                        passwordErrorMessage.value = it.second
                    }
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            //确认密码输入框
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                    // 检查密码是否匹配
                    checkPasswordMatch(password.value, confirmPassword.value).let {
                        passwordError.value = it.first
                        passwordErrorMessage.value = it.second
                    }
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
                //错误时红色高亮
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (passwordError.value) Color.Red else Color(0xFF2575FC),
                    unfocusedIndicatorColor = if (passwordError.value) Color.Red else Color.LightGray,
                    focusedLabelColor = if (passwordError.value) Color.Red else Color(0xFF2575FC),
                    cursorColor = if (passwordError.value) Color.Red else Color(0xFF2575FC)
                ),
                isError = passwordError.value
            )
            //错误提示
            if (passwordError.value) {
                Text(
                    text = passwordErrorMessage.value,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // 提交前验证所有字段
                    val emailValidation = validateEmail(email.value)
                    val phoneValidation = validatePhoneNumber(phone.value)
                    val passwordValidation = checkPasswordMatch(password.value, confirmPassword.value)

                    emailError.value = emailValidation.first
                    phoneError.value = phoneValidation.first
                    passwordError.value = passwordValidation.first

                    formError.value = emailError.value || phoneError.value || passwordError.value

                    if (!formError.value) {
                        // 调用注册逻辑
                        viewModel.register(
                            username = username.value,
                            password = password.value,
                            email = email.value,
                            phone = phone.value
                        )
                        // 导航到登录页面
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                }
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


// 检查密码是否匹配的辅助函数
private fun checkPasswordMatch(password: String, confirmPassword: String): Pair<Boolean, String> {
    return when {
        password.isEmpty() && confirmPassword.isEmpty() ->
            false to ""

        password != confirmPassword ->
            true to "两次输入的密码不一致"

        password.length < 6 ->
            true to "密码长度至少为6位"

        else ->
            false to ""
    }
}
// 检查email是否正确的辅助函数
private fun validateEmail(email: String): Pair<Boolean, String> {
    return when {
        email.isEmpty() ->
            true to "邮箱不能为空"

        !email.contains("@") ->
            true to "邮箱格式不正确，缺少@符号"

        email.split("@").last().isEmpty() ||
                !email.split("@").last().contains(".") ->
            true to "邮箱格式不正确"

        else ->
            false to ""
    }
}
// 检查电话号码是否正确的辅助函数
private fun validatePhoneNumber(phone: String): Pair<Boolean, String> {
    return when {
        phone.isEmpty() ->
            true to "电话号码不能为空"

        phone.length != 11 ->
            true to "电话号码必须是11位数字"

        !phone.all { it.isDigit() } ->
            true to "电话号码必须为纯数字"

        else ->
            false to ""
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