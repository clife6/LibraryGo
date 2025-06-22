package com.clife.librarygo.ui.screen.home

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.clife.librarygo.viewmodel.home.ChangeAvatarViewModel
import kotlinx.coroutines.launch

@Composable
fun ChangeAvatarCompose(
    viewModel: ChangeAvatarViewModel = viewModel(),
    onAvatarSelected: (Uri) -> Unit = {},
    navController: NavHostController,
    id: String,
) {
    val context = LocalContext.current
    val imageUris = viewModel.imageUris
    val coroutineScope = rememberCoroutineScope()

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
    var hasPermission by remember {
        mutableStateOf(
            PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) viewModel.loadImages(context)
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(permission)
        else viewModel.loadImages(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部显示当前头像
        viewModel.selectedAvatarUri?.let { avatarUri ->
            AsyncImage(
                model = avatarUri,
                contentDescription = "当前头像",
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .size(120.dp),
                contentScale = ContentScale.Crop
            )
            // 选择头像按钮，点击后将当前头像Base64编码
            Button(
                onClick = {
                    viewModel.selectedAvatarUri?.let { uri ->
                        val base64Avatar = viewModel.encodeToBase64(context.contentResolver.openInputStream(uri)?.readBytes() ?: ByteArray(0))
                        coroutineScope.launch {
                            viewModel.updateAvatar(id,base64Avatar)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("选择头像")
            }
        }

        when {
            !hasPermission -> {
                Text("请授权访问相册")
            }
            imageUris.isEmpty() -> {
                Text("没有可用的头像，请添加图片")
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(imageUris.size) { idx ->
                        val uri = imageUris[idx]
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectAvatar(uri)
                                    onAvatarSelected(uri)
                                }
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun PreviewChangeAvatarCompose() {
//    LibraryGoTheme {
//        ChangeAvatarCompose(
//            navController = rememberNavController(),
//            id = "2"
//
//        )
//    }
//}