package com.example.moviecatalog.data.model.movie

import com.example.moviecatalog.data.model.review.ReviewModel
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsModel(
    val id: String,
    val name: String? = null,
    val poster: String? = null,
    val year: Int?,
    val country: String? = null,
    val genres: List<GenreModel>? = null,
    val reviews: List<ReviewModel>? = null,
    val time: Int?,
    val tagline: String? = null,
    val description: String? = null,
    val director: String? = null,
    val budget: Int? = null,
    val fees: Int? = null,
    val ageLimit: Int?
)
