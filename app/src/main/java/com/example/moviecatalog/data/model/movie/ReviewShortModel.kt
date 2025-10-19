package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class ReviewShortModel(
    val id: String,
    val rating: Int
)
