package com.clife.librarygo.ui.screen.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.clife.librarygo.R
import com.clife.librarygo.navigation.Routes
import com.clife.librarygo.viewmodel.home.HomeViewModel
import com.clife.librarygo.viewmodel.home.ProfileTabContentViewModel

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
    navController: NavController,
    paddingValues: PaddingValues,
    id: String,
    viewModel : ProfileTabContentViewModel = viewModel(),
    home : HomeViewModel
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    LaunchedEffect(id) {
        viewModel.getUserInfo(id)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .background(Color(0xFFD3D2D2))
    ){
        Row(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(20.dp)
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                //如果userInfo.avatar为空则使用默认头像
                model = userInfo?.avatar ?: R.drawable.default_avatar,
                contentDescription = "头像",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable{
                        // 跳转到更改头像界面
                        navController.navigate(Routes.CHANGE_AVATAR)
                    }
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column{
                userInfo?.let {
                    Text(
                        text = it.username,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Text(
                    text = "ID:${id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "查看详情",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable{
                    // 跳转到更改头像界面
                    navController.navigate(Routes.CHANGE_AVATAR)
                }
            )
        }
        Spacer(
            modifier = Modifier
                .height(10.dp) // 需要指定高度，否则 Spacer 没有可见区域
        )
        Row(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(15.dp)
                .height(25.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.Star,
                contentDescription = "我的收藏",
                tint = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = "我的收藏",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "查看收藏",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable{
                    /*跳转我的收藏界面*/
                }
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Row(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(15.dp)
                .height(25.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.Book,
                contentDescription = "我的借阅",
                tint = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = "我的借阅",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "跳转书架",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable{
                    // 跳转到书架界面
                    home.selectTab(2)
                }
            )

        }
    }
}

//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//
//@Composable
//fun PreviewProfileTabContent() {
//    LibraryGoTheme {
//        ProfileTabContent(
//            navController = rememberNavController(),
//            paddingValues = PaddingValues(0.dp),
//            "1",
//
//        )
//    }
//}