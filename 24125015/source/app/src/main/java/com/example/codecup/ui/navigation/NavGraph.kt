package com.example.codecup.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codecup.ui.home.HomeScreen
import com.example.codecup.ui.screens.*

import androidx.navigation.NavGraph.Companion.findStartDestination

// ui_design §6: pushed screens slide in from the right; top-level (tab/drawer)
// destinations cross-fade since they're peers, not a stack. Keep it under 250ms.
private const val NAV_ANIM_MS = 240

private fun fadeInSpec(): EnterTransition = fadeIn(animationSpec = tween(NAV_ANIM_MS))
private fun fadeOutSpec(): ExitTransition = fadeOut(animationSpec = tween(NAV_ANIM_MS))

private val fadeEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = { fadeInSpec() }
private val fadeExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = { fadeOutSpec() }

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = {
            slideInHorizontally(animationSpec = tween(NAV_ANIM_MS)) { fullWidth -> fullWidth } + fadeInSpec()
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(NAV_ANIM_MS)) { fullWidth -> -fullWidth / 4 } + fadeOutSpec()
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(NAV_ANIM_MS)) { fullWidth -> -fullWidth / 4 } + fadeInSpec()
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(NAV_ANIM_MS)) { fullWidth -> fullWidth } + fadeOutSpec()
        }
    ) {
        composable(
            "splash",
            enterTransition = fadeEnter,
            exitTransition = fadeExit
        ) {
            SplashScreen(onTimeout = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable(
            "home",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
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

        composable(
            "favorites",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
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
                    // Pop Details/Cart off the stack so back from Success goes Home,
                    // never into the now-empty cart (ui_design §3.4)
                    navController.navigate("success/$orderId") {
                        popUpTo("home")
                    }
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
                    // Land on My Orders with the just-placed order highlighted
                    navController.navigate("orders?highlight=$orderId") {
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
        
        composable(
            route = "orders?highlight={highlightId}",
            arguments = listOf(
                navArgument("highlightId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) { backStackEntry ->
            MyOrdersScreen(
                highlightOrderId = backStackEntry.arguments?.getString("highlightId"),
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
        
        composable(
            "rewards",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
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
        
        composable(
            "profile",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
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

        composable("barista") {
            BaristaScreen(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("details/$productId")
                }
            )
        }

        composable(
            "notifications",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
            NotificationsScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            "about",
            enterTransition = fadeEnter,
            exitTransition = fadeExit,
            popEnterTransition = fadeEnter,
            popExitTransition = fadeExit
        ) {
            AboutScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
