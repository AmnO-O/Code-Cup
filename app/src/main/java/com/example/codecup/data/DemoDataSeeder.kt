package com.example.codecup.data

import com.example.codecup.data.database.AppDatabase
import com.example.codecup.data.database.NotificationEntity
import com.example.codecup.data.database.OrderEntity
import com.example.codecup.data.database.OrderItemEntity
import com.example.codecup.data.database.PointsHistoryEntity
import com.example.codecup.data.database.SeedData
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.first

/**
 * One-time first-launch seed of a believable demo account, so the app never opens
 * onto all-empty screens: five completed orders (with the exact stamps and points
 * they would have earned), one redemption, one order already "Ready" for pickup —
 * letting the Ongoing -> History transition and "+1 stamp" be demoed immediately —
 * and a couple of notifications. Amounts are all derived from PriceCalculator /
 * RewardsRepository constants so the ledger stays arithmetically consistent.
 */
class DemoDataSeeder(
    private val database: AppDatabase,
    private val rewardsRepository: RewardsRepository
) {

    private data class DemoLine(val product: Product, val quantity: Int, val size: String, val shots: String, val ice: String) {
        val linePrice: Double
            get() = PriceCalculator.totalPrice(product.price, size, shots, quantity, product.category)
    }

    private data class DemoOrder(val id: String, val daysAgo: Int, val lines: List<DemoLine>) {
        val total: Double get() = lines.sumOf { it.linePrice }
    }

    suspend fun seedIfNeeded(now: Long = System.currentTimeMillis()) {
        if (rewardsRepository.isDemoSeeded()) return
        // Never overwrite a user who already has real data (e.g. upgraded install)
        if (database.orderDao().orderCount().first() > 0) {
            rewardsRepository.seedDemoBalances(stamps = 0, points = 0)
            return
        }

        val menu = SeedData.products.associateBy { it.name }
        fun product(name: String): Product = menu.getValue(name)
        fun drink(name: String, qty: Int = 1, size: String = PriceCalculator.SIZE_MEDIUM, shots: String = PriceCalculator.SHOTS_DOUBLE) =
            DemoLine(product(name), qty, size, shots, PriceCalculator.ICE_REGULAR)
        fun pastry(name: String, qty: Int = 1) =
            DemoLine(product(name), qty, PriceCalculator.SIZE_SMALL, PriceCalculator.SHOTS_SINGLE, PriceCalculator.ICE_NONE)

        val completedOrders = listOf(
            DemoOrder("AC-10412", daysAgo = 12, lines = listOf(drink("Artisan Cappuccino"), pastry("Butter Croissant"))),
            DemoOrder("AC-10418", daysAgo = 9, lines = listOf(drink("Nitro Cold Brew", qty = 2, size = PriceCalculator.SIZE_LARGE))),
            DemoOrder("AC-10423", daysAgo = 7, lines = listOf(drink("Caramel Macchiato", shots = PriceCalculator.SHOTS_TRIPLE))),
            DemoOrder("AC-10429", daysAgo = 4, lines = listOf(drink("Oat Milk Latte"), pastry("Almond Croissant"))),
            DemoOrder("AC-10433", daysAgo = 2, lines = listOf(drink("Iced Americano", size = PriceCalculator.SIZE_LARGE)))
        )
        // Waiting in the Ongoing tab, already Ready: one tap demoes the
        // status transition + stamp award end-to-end.
        val readyOrder = DemoOrder("AC-10437", daysAgo = 0, lines = listOf(drink("Artisan Cappuccino")))

        val orderDao = database.orderDao()
        (completedOrders + readyOrder).forEach { demo ->
            val dateMillis = now - demo.daysAgo * DAY_MILLIS
            val status = if (demo === readyOrder) OrderStatus.Ready else OrderStatus.PickedUp
            orderDao.placeOrder(
                OrderEntity(id = demo.id, dateMillis = dateMillis, totalPrice = demo.total, status = status.name),
                demo.lines.map { line ->
                    OrderItemEntity(
                        orderId = demo.id,
                        productId = line.product.id,
                        productName = line.product.name,
                        imageUrl = line.product.imageUrl,
                        quantity = line.quantity,
                        size = line.size,
                        shots = line.shots,
                        iceLevel = line.ice,
                        linePrice = line.linePrice
                    )
                }
            )
        }

        // Points ledger mirrors the completed orders exactly, plus one redemption
        val historyDao = database.pointsHistoryDao()
        var pointsBalance = 0
        completedOrders.forEach { demo ->
            val earned = (demo.total * RewardsRepository.POINTS_PER_DOLLAR).toInt()
            pointsBalance += earned
            historyDao.insert(
                PointsHistoryEntity(title = "Order #${demo.id}", dateMillis = now - demo.daysAgo * DAY_MILLIS, points = earned)
            )
        }
        val croissant = product("Butter Croissant")
        val redemptionCost = RewardsRepository.redeemCostFor(croissant.price)
        pointsBalance -= redemptionCost
        historyDao.insert(
            PointsHistoryEntity(title = "Redeemed ${croissant.name}", dateMillis = now - DAY_MILLIS, points = -redemptionCost)
        )

        val notificationDao = database.notificationDao()
        notificationDao.insert(
            NotificationEntity(
                title = "Stamp earned",
                body = "Order #${completedOrders.last().id} completed — +1 stamp!",
                dateMillis = now - 2 * DAY_MILLIS,
                isRead = true
            )
        )
        notificationDao.insert(
            NotificationEntity(
                title = "Your coffee is ready! ☕",
                body = "Order #${readyOrder.id} is ready for pickup. See you soon!",
                dateMillis = now - HOUR_MILLIS,
                isRead = false
            )
        )

        rewardsRepository.seedDemoBalances(stamps = completedOrders.size, points = pointsBalance)
    }

    companion object {
        private const val DAY_MILLIS = 24 * 60 * 60 * 1000L
        private const val HOUR_MILLIS = 60 * 60 * 1000L
    }
}
