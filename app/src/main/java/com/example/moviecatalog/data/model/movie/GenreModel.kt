package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class GenreModel(
    val id: String,
    val name: String? = null
)
