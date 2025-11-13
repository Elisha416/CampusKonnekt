package com.example.campuskonnekt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.campuskonnekt.ui.screens.auth.LoginScreen
import com.example.campuskonnekt.ui.screens.auth.RegisterScreen
import com.example.campuskonnekt.ui.screens.main.MainScreen

@Composable
fun NavGraph(startDestination: String = "auth") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(startDestination = "login", route = "auth") {
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onRegisterSuccess = {
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }
        }
        composable("main") {
            MainScreen()
        }
    }
}
