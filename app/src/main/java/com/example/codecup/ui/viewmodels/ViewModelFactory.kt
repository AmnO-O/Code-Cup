package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codecup.data.*

class ViewModelFactory(
    private val context: Context? = null,
    private val productId: Int = -1,
    private val productRepository: ProductRepository = ProductRepository(),
    private val cartRepository: CartRepository = CartRepository.getInstance(),
    private val orderRepository: OrderRepository = OrderRepository.getInstance(),
    private val profileRepository: ProfileRepository = ProfileRepository.getInstance(),
    private val favoritesRepository: FavoritesRepository? = context?.let { FavoritesRepository.getInstance(it) },
    private val userPreferencesRepository: UserPreferencesRepository? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPreferencesRepository!!) as T
            }
            modelClass.isAssignableFrom(ProductDetailsViewModel::class.java) -> {
                ProductDetailsViewModel(productId, productRepository, cartRepository, favoritesRepository!!) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(productRepository, cartRepository, profileRepository, favoritesRepository!!) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(favoritesRepository!!, productRepository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(cartRepository, orderRepository, profileRepository, context) as T
            }
            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> {
                MyOrdersViewModel(orderRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository, userPreferencesRepository!!) as T
            }
            modelClass.isAssignableFrom(RewardsViewModel::class.java) -> {
                RewardsViewModel(profileRepository) as T
            }
            modelClass.isAssignableFrom(RedeemRewardsViewModel::class.java) -> {
                RedeemRewardsViewModel(profileRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
