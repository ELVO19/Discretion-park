package com.example.carparkingsystem.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.ui.theme.screens.dashboard.Dashboard
import com.example.carparkingsystem.ui.theme.screens.login.LoginScreen
import com.example.carparkingsystem.ui.theme.screens.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),
               startDestination: String = ROUTE_REGISTER){
    NavHost(navController = navController, startDestination = startDestination ){
        composable(ROUTE_REGISTER){ RegisterScreen(navController) }
        composable(ROUTE_LOGIN){ LoginScreen(navController) }
        composable(ROUTE_DASHBOARD){ Dashboard(navController) }
    }

}