package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class MoviesListModel(
    val movies: List<MovieElementModel>
)
