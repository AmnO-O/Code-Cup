package com.example.codecup.data

import com.example.codecup.models.PointsHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserProfile(
    val name: String = "Alex Johnson",
    val email: String = "alex.johnson@example.com",
    val phone: String = "+1 (555) 123-4567",
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuAl3oL7Ez9iBCbgtJa5MHZ4oztckVgwX8ME-c7JHzd7GUNKxhcjFfdY8TxbHWuhqwiY7Vw8NmjC-l6D6-_FKJgSK0sCrcQAleIINZIym-2nsHOz-e6v3JbksXLs7q7HhLoGTf1laO1OY6IdGM8uOEPwnnCYGJ2wJtYNysZ1g8xlCub13bU1Y8J9-ECTUsQlNQSIdXY7LJ-j3VVkYtq6gCL1gVUmnbXZHDHDjMyj8B0k0TEOBuaz_p1t",
    val ordersCount: Int = 42,
    val points: Int = 1240,
    val stamps: Int = 7,
    val freeDrinks: Int = 0,
    val joinedYear: String = "2023",
    val pointsHistory: List<PointsHistoryItem> = listOf(
        PointsHistoryItem("Order #1042", "21 July, 21:15", "+8 pts", true),
        PointsHistoryItem("Order #1041", "20 July, 09:30", "+5 pts", true),
        PointsHistoryItem("Redeemed Caramel Macchiato", "18 July, 14:20", "-150 pts", false),
        PointsHistoryItem("Order #1039", "18 July, 10:15", "+12 pts", true)
    )
)

class ProfileRepository {
    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    fun updateProfile(name: String, email: String, phone: String) {
        _profile.update { it.copy(name = name, email = email, phone = phone) }
    }

    fun updateAvatar(newUrl: String) {
        _profile.update { it.copy(avatarUrl = newUrl) }
    }

    fun addPoints(amount: Int, title: String) {
        val date = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(Date())
        val newItem = PointsHistoryItem(title, date, "+$amount pts", true)
        _profile.update { 
            it.copy(
                points = it.points + amount,
                pointsHistory = listOf(newItem) + it.pointsHistory
            )
        }
    }

    fun redeemPoints(amount: Int, title: String) {
        val date = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(Date())
        val newItem = PointsHistoryItem(title, date, "-$amount pts", false)
        _profile.update { 
            it.copy(
                points = it.points - amount,
                pointsHistory = listOf(newItem) + it.pointsHistory
            )
        }
    }

    fun addStamp() {
        _profile.update { 
            val newStamps = it.stamps + 1
            if (newStamps >= 8) {
                it.copy(stamps = 8, ordersCount = it.ordersCount + 1)
            } else {
                it.copy(stamps = newStamps, ordersCount = it.ordersCount + 1)
            }
        }
    }

    fun resetStamps() {
        _profile.update { 
            if (it.stamps >= 8) {
                it.copy(stamps = 0, freeDrinks = it.freeDrinks + 1)
            } else {
                it
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ProfileRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository().also { instance = it }
            }
    }
}
