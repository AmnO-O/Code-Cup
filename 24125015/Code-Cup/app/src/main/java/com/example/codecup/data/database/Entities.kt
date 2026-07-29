package com.example.codecup.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.codecup.models.Product

/** One customized drink in the cart. Product display data is joined from the products table. */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val productId: Int,
    val quantity: Int,
    val size: String,
    val shots: String,
    val iceLevel: String,
    val totalPrice: Double
)

data class CartItemWithProduct(
    @Embedded val item: CartItemEntity,
    @Relation(parentColumn = "productId", entityColumn = "id")
    val product: Product
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val dateMillis: Long,
    val totalPrice: Double,
    val status: String,
    val deliveryAddress: String = "123 Artisan Lane, Coffee City"
)

/** Line items snapshot the product name/image so history stays intact even if the menu changes. */
@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val itemId: Long = 0,
    val orderId: String,
    val productId: Int,
    val productName: String,
    val imageUrl: String,
    val quantity: Int,
    val size: String,
    val shots: String,
    val iceLevel: String,
    val linePrice: Double
)

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "id", entityColumn = "orderId")
    val items: List<OrderItemEntity>
)

/** Signed points delta: positive = earned, negative = redeemed. */
@Entity(tableName = "points_history")
data class PointsHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateMillis: Long,
    val points: Int
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val dateMillis: Long,
    val isRead: Boolean = false
)
