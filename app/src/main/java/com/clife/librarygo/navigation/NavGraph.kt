package com.clife.librarygo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.clife.librarygo.ui.screen.login.AdminLoginCompose
import com.clife.librarygo.ui.screen.login.LoginCompose
import com.clife.librarygo.ui.screen.WelcomeCompose
import com.clife.librarygo.ui.screen.home.BookDetailCompose
import com.clife.librarygo.ui.screen.home.BookshelfTabContent
import com.clife.librarygo.ui.screen.home.ChangeAvatarCompose
import com.clife.librarygo.ui.screen.home.HomeCompose
import com.clife.librarygo.ui.screen.login.RegisterCompose
import com.clife.librarygo.ui.screen.login.RetrievePasswordCompose
import com.clife.librarygo.ui.screen.login.SetNewPasswordCompose


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
        composable(
            "${Routes.HOME}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            HomeCompose(
                navController = navController,
                id = id
            )
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
            "${Routes.SET_NEW_PASSWORD}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            SetNewPasswordCompose(
                navController = navController,
                id = id
            )
        }
        composable(
            "${Routes.BOOK_DETAIL}/{id}/{uid}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            BookDetailCompose(
                navController = navController,
                id = id,
                uid = uid
            )
        }
        composable(Routes.CHANGE_AVATAR) {
            ChangeAvatarCompose(navController = navController)
        }
    }
}
