package com.mercel.mealmate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mercel.mealmate.presentation.auth.screen.AuthScreen
import com.mercel.mealmate.presentation.detail.screen.DetailScreen
import com.mercel.mealmate.presentation.discover.screen.DiscoverScreen
import com.mercel.mealmate.presentation.home.screen.HomeScreen
import com.mercel.mealmate.presentation.instantmeal.screen.InstantMealScreen
import com.mercel.mealmate.presentation.plan.screen.PlanScreen
import com.mercel.mealmate.presentation.profile.screen.ProfileScreen
import com.mercel.mealmate.presentation.settings.screen.SettingsScreen
import com.mercel.mealmate.presentation.shopping.screen.ShoppingScreen

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Discover : Screen("discover")
    object InstantMeal : Screen("instant_meal")
    object Detail : Screen("detail/{recipeId}") {
        fun createRoute(recipeId: String) = "detail/$recipeId"
    }
    object Plan : Screen("plan")
    object Shopping : Screen("shopping")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDiscover = {
                    navController.navigate(Screen.Discover.route)
                },
                onNavigateToInstantMealMaker = {
                    navController.navigate(Screen.InstantMeal.route)
                }
            )
        }
        
        composable(Screen.Discover.route) {
            DiscoverScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                }
            )
        }
        
        composable(Screen.InstantMeal.route) {
            InstantMealScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("recipeId") { type = NavType.StringType }
            )
        ) {
            DetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddToPlan = { recipeId ->
                    // Navigate to plan screen
                    navController.navigate(Screen.Plan.route)
                }
            )
        }

        composable(Screen.Plan.route) {
            PlanScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                }
            )
        }

        composable(Screen.Shopping.route) {
            ShoppingScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
