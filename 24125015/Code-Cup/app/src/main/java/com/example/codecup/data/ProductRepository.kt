package com.example.codecup.data

import com.example.codecup.data.database.ProductDao
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    fun getProducts(): Flow<List<Product>> = productDao.getAll()

    suspend fun getProductById(id: Int): Product? = productDao.getById(id)
}
