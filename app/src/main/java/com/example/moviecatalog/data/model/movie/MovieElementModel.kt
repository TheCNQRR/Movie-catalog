package com.example.moviecatalog.data.model.movie

import android.os.Parcelable
import com.example.moviecatalog.data.model.review.ReviewShortModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class MovieElementModel(
    val id: String,
    val name: String,
    val poster: String,
    val year: Int,
    val country: String,
    val genres: List<GenreModel>,
    val reviews: List<ReviewShortModel>
) : Parcelable
