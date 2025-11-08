@file:Suppress("DEPRECATION")

package com.example.moviecatalog.logic.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.CollectionModel
import kotlinx.serialization.json.Json

class CollectionsManager {
    private val json = Json { ignoreUnknownKeys = true }

    private fun getPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.collections_storage),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getCollections(context: Context): List<CollectionModel> {
        val preferences = getPreferences(context)
        val collectionsJson = preferences.getString(context.getString(R.string.user_collections), null) ?: return emptyList()

        return try {
            json.decodeFromString<List<CollectionModel>>(collectionsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getDefaultCollections(context: Context): List<CollectionModel> {
        return listOf(
            CollectionModel(
                id = "favorites",
                name = context.getString(R.string.favourite),
                avatarUrl = "android.resource://${context.packageName}/${R.drawable.favorite_collection_icon}",
                movieIds = emptyList(),
                )
        )
    }

    fun getCollectionsWithDefaults(context: Context): List<CollectionModel> {
        val savedCollections = getCollections(context)
        val defaultCollections = getDefaultCollections(context)

        return (defaultCollections + savedCollections).distinctBy { it.id }
    }
}