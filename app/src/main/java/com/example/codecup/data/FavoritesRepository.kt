package com.example.codecup.data

import com.example.codecup.data.database.FavoritesDao
import com.example.codecup.models.FavoriteProduct
import kotlinx.coroutines.flow.Flow

class FavoritesRepository private constructor(private val favoritesDao: FavoritesDao) {
    fun getAllFavorites(): Flow<List<FavoriteProduct>> = favoritesDao.getAllFavorites()

    suspend fun addFavorite(productId: Int) {
        favoritesDao.insertFavorite(FavoriteProduct(productId))
    }

    suspend fun removeFavorite(productId: Int) {
        favoritesDao.deleteFavorite(FavoriteProduct(productId))
    }

    fun isFavorite(productId: Int): Flow<Boolean> = favoritesDao.isFavorite(productId)

    companion object {
        @Volatile
        private var instance: FavoritesRepository? = null

        fun getInstance(context: android.content.Context): FavoritesRepository {
            return instance ?: synchronized(this) {
                val database = com.example.codecup.data.database.AppDatabase.getDatabase(context)
                FavoritesRepository(database.favoritesDao()).also { instance = it }
            }
        }
    }
}
