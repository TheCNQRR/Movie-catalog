package com.example.moviecatalog.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionModel(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val movieIds: List<String> = emptyList(),
)