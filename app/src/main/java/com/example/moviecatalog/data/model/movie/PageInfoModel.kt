package com.example.moviecatalog.data.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoModel(
    val pageSize: Int?,
    val pageCount: Int?,
    val currentPage: Int?
)
