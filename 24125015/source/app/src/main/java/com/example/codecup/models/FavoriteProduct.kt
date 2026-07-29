package com.example.codecup.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteProduct(
    @PrimaryKey val productId: Int,
    val dateAdded: Long = System.currentTimeMillis()
)
