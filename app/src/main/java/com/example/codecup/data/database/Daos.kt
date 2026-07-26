package com.example.codecup.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Int): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)
}

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemWithProduct>>

    @Query(
        "SELECT * FROM cart_items WHERE productId = :productId AND size = :size " +
            "AND shots = :shots AND iceLevel = :iceLevel LIMIT 1"
    )
    suspend fun findMatching(productId: Int, size: String, shots: String, iceLevel: String): CartItemEntity?

    @Query("SELECT * FROM cart_items WHERE id = :id")
    suspend fun getById(id: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)

    @Update
    suspend fun update(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clear()
}

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders ORDER BY dateMillis DESC")
    fun getOrders(): Flow<List<OrderWithItems>>

    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun placeOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        insertOrder(order)
        insertOrderItems(items)
    }

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: String, status: String)

    @Query("SELECT COUNT(*) FROM orders")
    fun orderCount(): Flow<Int>
}

@Dao
interface PointsHistoryDao {
    @Query("SELECT * FROM points_history ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<PointsHistoryEntity>>

    @Insert
    suspend fun insert(entry: PointsHistoryEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun unreadCount(): Flow<Int>

    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllRead()
}
