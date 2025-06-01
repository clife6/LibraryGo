package com.clife.librarygo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clife.librarygo.compose.HomeCompose
import com.clife.librarygo.compose.LoginCompose
import com.clife.librarygo.compose.RegisterCompose
import com.clife.librarygo.compose.WelcomeCompose

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
    }
}