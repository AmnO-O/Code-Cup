package com.example.codecup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codecup.ui.home.HomeScreen
import com.example.codecup.ui.screens.*

import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        
        composable("home") {
            HomeScreen(
                onProductClick = { productId ->
                    navController.navigate("details/$productId")
                },
                onNavigateToRewards = {
                    navController.navigate("rewards") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigate = { route ->
                    if (route != "home") {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                onProductClick = { productId ->
                    navController.navigate("details/$productId")
                },
                onNavigate = { route ->
                    if (route != "favorites") {
                        if (route == "home") {
                            navController.popBackStack("home", inclusive = false)
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
        
        composable(
            route = "details/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailsScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onAddToCartClick = {
                    navController.navigate("cart")
                }
            )
        }
        
        composable("cart") {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = { orderId ->
                    navController.navigate("success/$orderId")
                }
            )
        }
        
        composable(
            route = "success/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderSuccessScreen(
                orderId = orderId,
                onTrackOrderClick = {
                    navController.navigate("orders") {
                        popUpTo("home") { saveState = true }
                    }
                },
                onBackToHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        
        composable("orders") {
            MyOrdersScreen(
                onNavigate = { route ->
                    if (route != "orders") {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
        
        composable("rewards") {
            RewardsScreen(
                onNavigate = { route ->
                    if (route != "rewards") {
                        if (route == "home") {
                            navController.popBackStack("home", inclusive = false)
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                onRedeemClick = {
                    navController.navigate("redeem")
                }
            )
        }
        
        composable("redeem") {
            RedeemRewardsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("profile") {
            ProfileScreen(
                onNavigate = { route ->
                    if (route != "profile") {
                        if (route == "home") {
                            navController.popBackStack("home", inclusive = false)
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    }
}
