package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.codecup.CodeCupApplication

/**
 * Manual DI: resolves every ViewModel from the process-wide [com.example.codecup.data.AppContainer]
 * owned by the Application, so all screens share the same repository instances.
 */
class ViewModelFactory(
    private val context: Context,
    private val productId: Int = -1
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = context.applicationContext as CodeCupApplication
        val c = app.container
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(c.userPreferencesRepository, c.profileRepository, c.rewardsRepository, c.notificationsRepository) as T
            }
            modelClass.isAssignableFrom(ProductDetailsViewModel::class.java) -> {
                ProductDetailsViewModel(
                    productId,
                    c.productRepository,
                    c.cartRepository,
                    c.favoritesRepository,
                    extras.createSavedStateHandle()
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(c.productRepository, c.cartRepository, c.profileRepository, c.rewardsRepository, c.favoritesRepository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(c.favoritesRepository, c.productRepository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(c.cartRepository, c.orderRepository, c.rewardsRepository, c.notificationsRepository, app) as T
            }
            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> {
                MyOrdersViewModel(c.orderRepository, c.rewardsRepository, c.cartRepository, c.notificationsRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(c.profileRepository, c.rewardsRepository, c.orderRepository, c.userPreferencesRepository) as T
            }
            modelClass.isAssignableFrom(RewardsViewModel::class.java) -> {
                RewardsViewModel(c.rewardsRepository, c.orderRepository, c.productRepository, app) as T
            }
            modelClass.isAssignableFrom(RedeemRewardsViewModel::class.java) -> {
                RedeemRewardsViewModel(c.rewardsRepository, c.orderRepository, c.productRepository, c.notificationsRepository, app) as T
            }
            modelClass.isAssignableFrom(BaristaViewModel::class.java) -> {
                BaristaViewModel(c.productRepository, c.cartRepository) as T
            }
            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> {
                NotificationsViewModel(c.notificationsRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
