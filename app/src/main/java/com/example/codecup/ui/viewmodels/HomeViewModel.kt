package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = listOf("All Coffee", "Espresso", "Cold Brew", "Pastries"),
    val selectedCategory: String = "All Coffee",
    val searchQuery: String = "",
    val cartItemsCount: Int = 0,
    val stampsEarned: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    init {
        loadProducts()
        observeCart()
        observeProfile()
    }

    private fun loadProducts() {
        productRepository.getProducts()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { products ->
                _allProducts.value = products
                applyFilters()
            }
            .launchIn(viewModelScope)
    }

    private fun observeCart() {
        cartRepository.cartItems.onEach { items ->
            _uiState.update { it.copy(cartItemsCount = items.size) }
        }.launchIn(viewModelScope)
    }

    private fun observeProfile() {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(stampsEarned = user.stamps) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun applyFilters() {
        val category = _uiState.value.selectedCategory
        val query = _uiState.value.searchQuery

        val filtered = _allProducts.value.filter { product ->
            val matchesCategory = category == "All Coffee" || product.category == category
            val matchesSearch = query.isBlank() || product.name.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }
        _uiState.update { it.copy(products = filtered, isLoading = false) }
    }

    fun quickAddToCart(product: Product) {
        val cartItem = CartItem(
            product = product,
            quantity = 1,
            size = "Medium (12oz)",
            shots = "Double",
            iceLevel = "Regular Ice",
            totalPrice = product.price + 0.50 // Default medium price
        )
        viewModelScope.launch {
            cartRepository.addToCart(cartItem)
        }
    }
}
