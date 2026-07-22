package com.example.codecup.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserProfile(
    val name: String = "Alex Johnson",
    val email: String = "alex.johnson@example.com",
    val phone: String = "+1 (555) 123-4567",
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuAl3oL7Ez9iBCbgtJa5MHZ4oztckVgwX8ME-c7JHzd7GUNKxhcjFfdY8TxbHWuhqwiY7Vw8NmjC-l6D6-_FKJgSK0sCrcQAleIINZIym-2nsHOz-e6v3JbksXLs7q7HhLoGTf1laO1OY6IdGM8uOEPwnnCYGJ2wJtYNysZ1g8xlCub13bU1Y8J9-ECTUsQlNQSIdXY7LJ-j3VVkYtq6gCL1gVUmnbXZHDHDjMyj8B0k0TEOBuaz_p1t",
    val ordersCount: Int = 42,
    val points: Int = 850,
    val joinedYear: String = "2023"
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

    companion object {
        @Volatile
        private var instance: ProfileRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository().also { instance = it }
            }
    }
}
