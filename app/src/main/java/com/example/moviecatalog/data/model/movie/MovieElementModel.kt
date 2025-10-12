package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class MovieElementModel(
    val id: String,
    val name: String? = null,
    val poster: String? = null,
    val year: Int?,
    val country: String? = null,
    val genreModels: List<GenreModel>? = null,
    val reviews: List<ReviewShortModel>? = null
)
