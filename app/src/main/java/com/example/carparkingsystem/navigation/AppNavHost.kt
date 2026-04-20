package com.example.carparkingsystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.carparkingsystem.ui.theme.screens.car.CarListScreen
import com.example.carparkingsystem.ui.theme.screens.car.UpdateCarScreen
import com.example.carparkingsystem.ui.theme.screens.dashboard.Dashboard
import com.example.carparkingsystem.ui.theme.screens.login.LoginScreen
import com.example.carparkingsystem.ui.theme.screens.register.RegisterScreen
import com.example.carparkingsystem.ui.theme.screens.splashScreen.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_SPLASH)    { SplashScreen(navController) }
        composable(ROUTE_REGISTER)  { RegisterScreen(navController) }
        composable(ROUTE_LOGIN)     { LoginScreen(navController) }
        composable(ROUTE_DASHBOARD) { Dashboard(navController) }
        composable(ROUTE_ADD_CAR) { UpdateCarScreen(navController, carId = "") }
        composable(ROUTE_CAR_LIST)  { CarListScreen(navController) }
        composable(
            route = ROUTE_UPDATE_CAR,
            arguments = listOf(navArgument("carId") { type = NavType.StringType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            UpdateCarScreen(navController = navController, carId = carId)
        }
    }
}