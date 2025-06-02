package com.clife.librarygo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.clife.librarygo.compose.AdminLoginCompose
import com.clife.librarygo.compose.LoginCompose
import com.clife.librarygo.compose.WelcomeCompose
import com.clife.librarygo.compose.HomeCompose
import com.clife.librarygo.compose.RegisterCompose
import com.clife.librarygo.compose.RetrievePasswordCompose
import com.clife.librarygo.compose.SetNewPasswordCompose


@Composable
fun LibraryNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeCompose(navController = navController)
        }
        composable(Routes.LOGIN) {
            LoginCompose(navController = navController)
        }
        composable(Routes.HOME) {
            HomeCompose(navController = navController)
        }
        composable(Routes.REGISTER) {
            RegisterCompose(navController = navController)
        }
        composable(Routes.LOGIN_ADMIN) {
            AdminLoginCompose(navController = navController)
        }
        composable(Routes.RETRIEVE_PASSWORD) {
            RetrievePasswordCompose(navController = navController)
        }
        composable(
            "${Routes.SET_NEW_PASSWORD}/{uuid}",
            arguments = listOf(navArgument("uuid") { type = NavType.StringType })
        ) { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""
            SetNewPasswordCompose(
                navController = navController,
                uuid = uuid  // 传递非空参数
            )
        }
    }
}
