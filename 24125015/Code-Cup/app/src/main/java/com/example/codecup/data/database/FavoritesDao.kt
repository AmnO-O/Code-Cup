package com.example.codecup.data.database

import androidx.room.*
import com.example.codecup.models.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites ORDER BY dateAdded DESC")
    fun getAllFavorites(): Flow<List<FavoriteProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteProduct)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteProduct)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>
}
