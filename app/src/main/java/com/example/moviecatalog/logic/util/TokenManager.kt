package com.example.moviecatalog.logic.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.moviecatalog.R

class TokenManager {
    private fun getPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.token_storage),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(context: Context, token: String, expiresIn: Long? = null) {
        val preferences = getPreferences(context)
        preferences.edit {
            putString(context.getString(R.string.access_token), token)
        }
    }

    fun getToken(context: Context): String? {
        val preferences = getPreferences(context)
        val token = preferences.getString(context.getString(R.string.access_token), null) ?: return null
        return token
    }

    fun clearToken(context: Context) {
        val preferences = getPreferences(context)
        preferences.edit {
            remove(context.getString(R.string.access_token))
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != null
    }
}