package com.example.codecup.data

import com.example.codecup.models.Product
import com.example.codecup.models.sampleProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRepository {
    fun getProducts(): Flow<List<Product>> = flowOf(sampleProducts)
    
    fun getProductById(id: Int): Product? = sampleProducts.find { it.id == id }
}
