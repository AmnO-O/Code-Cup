package com.example.codecup.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_prefs")

data class UserProfile(
    val name: String = DEFAULT_NAME,
    val email: String = DEFAULT_EMAIL,
    val phone: String = DEFAULT_PHONE,
    val avatarUrl: String = DEFAULT_AVATAR_URL,
    val joinedYear: String = DEFAULT_JOINED_YEAR
) {
    companion object {
        const val DEFAULT_NAME = "Alex Johnson"
        const val DEFAULT_EMAIL = "alex.johnson@example.com"
        const val DEFAULT_PHONE = "+1 (555) 123-4567"
        const val DEFAULT_JOINED_YEAR = "2026"
        const val DEFAULT_AVATAR_URL =
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAl3oL7Ez9iBCbgtJa5MHZ4oztckVgwX8ME-c7JHzd7GUNKxhcjFfdY8TxbHWuhqwiY7Vw8NmjC-l6D6-_FKJgSK0sCrcQAleIINZIym-2nsHOz-e6v3JbksXLs7q7HhLoGTf1laO1OY6IdGM8uOEPwnnCYGJ2wJtYNysZ1g8xlCub13bU1Y8J9-ECTUsQlNQSIdXY7LJ-j3VVkYtq6gCL1gVUmnbXZHDHDjMyj8B0k0TEOBuaz_p1t"
    }
}

/** Identity data only — order stats and reward balances live in their own repositories. */
class ProfileRepository(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("profile_name")
        val EMAIL = stringPreferencesKey("profile_email")
        val PHONE = stringPreferencesKey("profile_phone")
        val AVATAR = stringPreferencesKey("profile_avatar")
    }

    val profile: Flow<UserProfile> = context.profileDataStore.data.map { prefs ->
        UserProfile(
            name = prefs[Keys.NAME] ?: UserProfile.DEFAULT_NAME,
            email = prefs[Keys.EMAIL] ?: UserProfile.DEFAULT_EMAIL,
            phone = prefs[Keys.PHONE] ?: UserProfile.DEFAULT_PHONE,
            avatarUrl = prefs[Keys.AVATAR] ?: UserProfile.DEFAULT_AVATAR_URL
        )
    }

    suspend fun updateProfile(name: String, email: String, phone: String) {
        context.profileDataStore.edit { prefs ->
            prefs[Keys.NAME] = name
            prefs[Keys.EMAIL] = email
            prefs[Keys.PHONE] = phone
        }
    }

    suspend fun updateAvatar(newUrl: String) {
        context.profileDataStore.edit { prefs -> prefs[Keys.AVATAR] = newUrl }
    }
}
