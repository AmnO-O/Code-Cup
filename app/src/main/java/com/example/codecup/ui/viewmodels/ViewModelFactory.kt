package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codecup.data.CartRepository
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.UserPreferencesRepository

class ViewModelFactory(
    private val productId: Int = -1,
    private val productRepository: ProductRepository = ProductRepository(),
    private val cartRepository: CartRepository = CartRepository.getInstance(),
    private val orderRepository: OrderRepository = OrderRepository.getInstance(),
    private val profileRepository: ProfileRepository = ProfileRepository.getInstance(),
    private val userPreferencesRepository: UserPreferencesRepository? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPreferencesRepository!!) as T
            }
            modelClass.isAssignableFrom(ProductDetailsViewModel::class.java) -> {
                ProductDetailsViewModel(productId, productRepository, cartRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(productRepository, cartRepository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(cartRepository, orderRepository) as T
            }
            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> {
                MyOrdersViewModel(orderRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository, userPreferencesRepository!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
