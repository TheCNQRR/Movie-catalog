package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class MoviesPagedListModel(
    val movies: List<MovieElementModel>,
    val pageInfo: PageInfoModel
)
