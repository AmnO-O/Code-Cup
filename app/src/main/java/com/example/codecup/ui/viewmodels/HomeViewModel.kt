package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.FavoritesRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = listOf("All Coffee", "Espresso", "Cold Brew", "Latte", "Pastries", "Cakes"),
    val selectedCategory: String = "All Coffee",
    val searchQuery: String = "",
    val cartItemsCount: Int = 0,
    val stampsEarned: Int = 0,
    val userName: String = "",
    val favoriteProductIds: Set<Int> = emptySet(),
    val isLoading: Boolean = true
)

/** Carries the just-added item so the snackbar's Undo can take it back out. */
data class QuickAddEvent(val item: CartItem)

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository,
    private val rewardsRepository: RewardsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _quickAddEvents = MutableSharedFlow<QuickAddEvent>()
    val quickAddEvents: SharedFlow<QuickAddEvent> = _quickAddEvents.asSharedFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    init {
        loadProducts()
        observeCart()
        observeProfile()
        observeRewards()
        observeFavorites()
    }

    private fun loadProducts() {
        productRepository.getProducts()
            .onEach { products ->
                _allProducts.value = products
                applyFilters(loaded = true)
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
            _uiState.update {
                it.copy(userName = user.name.split(" ").firstOrNull() ?: user.name)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeRewards() {
        rewardsRepository.stamps.onEach { stamps ->
            _uiState.update { it.copy(stampsEarned = stamps) }
        }.launchIn(viewModelScope)
    }

    private fun observeFavorites() {
        favoritesRepository.getAllFavorites().onEach { favorites ->
            _uiState.update { state -> state.copy(favoriteProductIds = favorites.map { it.productId }.toSet()) }
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

    fun clearFilters() {
        _uiState.update { it.copy(searchQuery = "", selectedCategory = "All Coffee") }
        applyFilters()
    }

    private fun applyFilters(loaded: Boolean = false) {
        val category = _uiState.value.selectedCategory
        val query = _uiState.value.searchQuery

        val filtered = _allProducts.value.filter { product ->
            val matchesCategory = category == "All Coffee" || product.category == category
            val matchesSearch = query.isBlank() || product.name.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }
        _uiState.update {
            it.copy(products = filtered, isLoading = if (loaded) false else it.isLoading)
        }
    }

    fun quickAddToCart(product: Product) {
        val size = if (product.category == "Cakes") PriceCalculator.SIZE_SLICE else PriceCalculator.SIZE_MEDIUM
        val cartItem = CartItem(
            product = product,
            quantity = 1,
            size = size,
            shots = PriceCalculator.SHOTS_DOUBLE,
            iceLevel = PriceCalculator.ICE_REGULAR,
            totalPrice = PriceCalculator.totalPrice(product.price, size, PriceCalculator.SHOTS_DOUBLE, 1, product.category)
        )
        viewModelScope.launch {
            cartRepository.addToCart(cartItem)
            _quickAddEvents.emit(QuickAddEvent(cartItem))
        }
    }

    fun undoQuickAdd(item: CartItem) {
        viewModelScope.launch {
            cartRepository.removeOneOf(item)
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            val isFav = _uiState.value.favoriteProductIds.contains(productId)
            if (isFav) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }
}
