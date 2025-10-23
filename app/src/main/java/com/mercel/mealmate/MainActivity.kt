package com.mercel.mealmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mercel.mealmate.presentation.auth.viewmodel.AuthViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercel.mealmate.presentation.navigation.NavGraph
import com.mercel.mealmate.presentation.navigation.Screen
import com.mercel.mealmate.ui.theme.MealMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealMateTheme {
                MealMateApp()
            }
        }
    }
}

@Composable
fun MealMateApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    
    // Determine start destination based on auth state
    val startDestination = if (authState.isLoggedIn) Screen.Home.route else Screen.Auth.route
    
    // Hide bottom bar on auth screen
    val showBottomBar = currentDestination?.route != Screen.Auth.route

    LaunchedEffect(authState.isLoggedIn) {
        if (!authState.isLoggedIn && currentDestination?.route != Screen.Auth.route) {
            navController.navigate(Screen.Auth.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                if (currentDestination?.route != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Home.route) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home"),
    BottomNavItem(Screen.Plan.route, Icons.Default.CalendarMonth, "Plan"),
    BottomNavItem(Screen.Shopping.route, Icons.Default.ShoppingCart, "Shopping"),
    BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Profile"),
    BottomNavItem(Screen.Settings.route, Icons.Default.Settings, "Settings")
)