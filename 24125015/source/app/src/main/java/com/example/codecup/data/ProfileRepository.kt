package com.example.codecup.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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
            "android.resource://com.example.codecup/drawable/hcmus_avatar"
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
        val avatarUrl = prefs[Keys.AVATAR]
        UserProfile(
            name = prefs[Keys.NAME] ?: UserProfile.DEFAULT_NAME,
            email = prefs[Keys.EMAIL] ?: UserProfile.DEFAULT_EMAIL,
            phone = prefs[Keys.PHONE] ?: UserProfile.DEFAULT_PHONE,
            avatarUrl = if (avatarUrl == null || avatarUrl.startsWith("https://lh3.googleusercontent.com")) {
                UserProfile.DEFAULT_AVATAR_URL
            } else {
                avatarUrl
            }
        )
    }

    suspend fun updateProfile(name: String, email: String, phone: String) {
        context.profileDataStore.edit { prefs ->
            prefs[Keys.NAME] = name
            prefs[Keys.EMAIL] = email
            prefs[Keys.PHONE] = phone
        }
    }

    /**
     * Copies a photo-picker image into app-internal storage and stores its file URI —
     * picker grants are temporary, so referencing the original URI would break after
     * a restart. A fresh filename per pick keeps Coil's cache from showing the old photo.
     */
    suspend fun updateAvatarFromUri(pickedUri: Uri): Boolean = withContext(Dispatchers.IO) {
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val copied = context.contentResolver.openInputStream(pickedUri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
            true
        } ?: false
        if (!copied) return@withContext false

        context.filesDir.listFiles { candidate ->
            candidate.name.startsWith("avatar_") && candidate.name != fileName
        }?.forEach { it.delete() }

        context.profileDataStore.edit { prefs ->
            prefs[Keys.AVATAR] = Uri.fromFile(file).toString()
        }
        true
    }
}
