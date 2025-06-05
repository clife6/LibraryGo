package com.clife.librarygo.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clife.librarygo.data.RetrofitClient
import com.clife.librarygo.data.request.UserRequest
import com.clife.librarygo.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

     // 导航事件
     private val _navigateTo = MutableSharedFlow<String>()
     val navigateTo = _navigateTo.asSharedFlow()

    //按钮加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

     // 表单状态
     data class RegisterFormState(
         val username: String = "",
         val password: String = "",
         val confirmPassword: String = "",
         val email: String = "",
         val phone: String = "",
         val usernameError: Boolean = false,
         val usernameErrorMessage: String = "",
         val passwordVisible: Boolean = false,
         val confirmPasswordVisible: Boolean = false,
         val passwordError: Boolean = false,
         val passwordErrorMessage: String = "",
         val emailError: Boolean = false,
         val emailErrorMessage: String = "",
         val phoneError: Boolean = false,
         val phoneErrorMessage: String = "",
         val formError: Boolean = false, // 表单是否有错误
         val registerError: Boolean = false, // 是否有注册错误
         val registerErrorMessage: String = "" // 注册错误信息
     )

     private val _formState =
         MutableStateFlow(RegisterFormState())//MutableStateFlow是一个可变的状态流，用于存储表单状态
     val formState = _formState.asStateFlow()//asStateFlow()将MutableStateFlow转换为不可变的状态流，供外部观察

     // 更新表单字段
     // 更新用户名
     fun updateUsername(username: String) {
         _formState.value = _formState.value.copy(username = username)
     }

     // 更新密码和确认密码
     fun updatePassword(password: String) {
         _formState.value = _formState.value.copy(
             password = password,
             passwordError = false,
             passwordErrorMessage = ""
         )
         validatePasswords()
     }

     // 更新确认密码
     fun updateConfirmPassword(confirmPassword: String) {
         _formState.value = _formState.value.copy(
             confirmPassword = confirmPassword,
             passwordError = false,
             passwordErrorMessage = ""
         )
         validatePasswords()
     }

     // 更新邮箱
     fun updateEmail(email: String) {
         _formState.value = _formState.value.copy(
             email = email,
             emailError = false,
             emailErrorMessage = ""
         )
         validateEmail()
     }

     // 更新手机号
     fun updatePhone(phone: String) {
         if (phone.all { it.isDigit() }) {
             _formState.value = _formState.value.copy(
                 phone = phone,
                 phoneError = false,
                 phoneErrorMessage = ""
             )
             validatePhone()
         }
     }

     // 切换密码的可见性
     fun togglePasswordVisibility() {
         _formState.value = _formState.value.copy(
             passwordVisible = !_formState.value.passwordVisible
         )
     }

     // 切换确认密码的可见性
     fun toggleConfirmPasswordVisibility() {
         _formState.value = _formState.value.copy(
             confirmPasswordVisible = !_formState.value.confirmPasswordVisible
         )
     }

     // 校验逻辑
     // 校验密码和确认密码
     private fun validatePasswords() {
         val state = _formState.value
         val result = when {
             state.password.isEmpty() && state.confirmPassword.isEmpty() ->
                 false to ""

             state.password != state.confirmPassword ->
                 true to "两次输入的密码不一致"

             state.password.length < 6 ->
                 true to "密码长度至少为6位"

             else -> false to ""
         }
         _formState.value = state.copy(
             passwordError = result.first,
             passwordErrorMessage = result.second
         )
     }

     // 校验邮箱
     private fun validateEmail() {
         val email = _formState.value.email
         val result = when {
             email.isEmpty() -> true to "邮箱不能为空"
             !email.contains("@") -> true to "邮箱格式不正确，缺少@符号"
             email.split("@").last().isEmpty() || !email.split("@").last()
                 .contains(".com") -> true to "邮箱格式不正确"

             else -> false to ""
         }
         _formState.value = _formState.value.copy(
             emailError = result.first,
             emailErrorMessage = result.second
         )
     }

     // 校验手机号
     private fun validatePhone() {
         val phone = _formState.value.phone
         val result = when {
             phone.isEmpty() -> true to "电话号码不能为空"
             phone.length != 11 -> true to "电话号码必须是11位数字"
             !phone.all { it.isDigit() } -> true to "电话号码必须为纯数字"
             else -> false to ""
         }
         _formState.value = _formState.value.copy(
             phoneError = result.first,
             phoneErrorMessage = result.second
         )
     }

     // 用户名不存在时的注册函数
     fun registerWithNoUsername(state: RegisterFormState) = viewModelScope.launch {
         try {
             val addUserResponse = RetrofitClient.userService.addUser(
                 UserRequest.AddUserRequest(
                     username = state.username,
                     password = state.password,
                     email = state.email,
                     phone = state.phone
                 )
             )
             if (addUserResponse.isSuccessful) {
                 addUserResponse.body()?.let {
                     when (it.code) {
                         1 -> {
                             // 注册成功，导航到登录页面
                             _navigateTo.emit(Routes.LOGIN)
                         }
                         else -> {
                             _formState.value = _formState.value.copy(
                                 registerErrorMessage = it.error ?: "注册失败，请稍后再试",
                                 registerError = true,
                             )
                         }
                     }
                 }
             } else {
                 _formState.value = _formState.value.copy(
                     registerErrorMessage = "网络请求失败，请稍后再试",
                     registerError = true,
                 )
                 _isLoading.value = false
                 return@launch
             }
             // 注册成功后清除错误状态
             _formState.value = state.copy(formError = false)
         } catch (e: Exception) {
             _formState.value.copy(registerErrorMessage = "错误信息: ${e.message}",
                 registerError = true,
             )
         }
     }

     //用户名存在时的注册函数
     fun registerWithUsername(){
         _formState.value = _formState.value.copy(
             usernameError = true,
             usernameErrorMessage = "用户名已存在",
             formError = true
         )
     }

     // 注册逻辑
     fun register() {
         validatePasswords()
         validateEmail()
         validatePhone()
         val state = _formState.value
         val hasError = state.passwordError || state.emailError || state.phoneError
         _formState.value = state.copy(formError = hasError) //如果有错误则设置formError为true
         viewModelScope.launch {
             // 注册按钮点击时的加载状态
             _isLoading.value = true
             // 只在注册时检查用户名是否存在
             try {
                 val checkUserResponse =
                     RetrofitClient.userService.searchUser(_formState.value.username)
                 if (checkUserResponse.isSuccessful) {
                     checkUserResponse.body()?.let {
                         when (it.code) {
                             1 -> registerWithUsername() // 用户名已存在
                             0 -> registerWithNoUsername(state) // 用户名不存在，进行注册
                         }
                     }
                 }
             } catch (e: Exception) {
                 _formState.value.copy(registerErrorMessage = "错误信息: ${e.message}",
                     registerError = true,
                     )
             }finally {
                 _isLoading.value = false // 无论成功与否，注册完成后都将加载状态设置为false
             }
         }
     }
 }