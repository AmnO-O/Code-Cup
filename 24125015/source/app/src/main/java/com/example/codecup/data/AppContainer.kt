package com.example.codecup.data

import android.content.Context
import com.example.codecup.data.database.AppDatabase

/** Manual dependency container — one instance per process, owned by the Application. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: AppDatabase by lazy { AppDatabase.getDatabase(appContext) }

    val productRepository: ProductRepository by lazy { ProductRepository(database.productDao()) }
    val cartRepository: CartRepository by lazy { CartRepository(database.cartDao()) }
    val orderRepository: OrderRepository by lazy { OrderRepository(database.orderDao()) }
    val rewardsRepository: RewardsRepository by lazy { RewardsRepository(appContext, database.pointsHistoryDao()) }
    val profileRepository: ProfileRepository by lazy { ProfileRepository(appContext) }
    val favoritesRepository: FavoritesRepository by lazy { FavoritesRepository(database.favoritesDao()) }
    val notificationsRepository: NotificationsRepository by lazy { NotificationsRepository(database.notificationDao()) }
    val userPreferencesRepository: UserPreferencesRepository by lazy { UserPreferencesRepository.getInstance(appContext) }
}
