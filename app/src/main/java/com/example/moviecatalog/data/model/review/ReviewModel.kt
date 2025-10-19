package com.example.moviecatalog.data.model.review

import com.example.moviecatalog.data.model.user.UserShortModel
import kotlinx.serialization.Serializable

@Serializable
data class ReviewModel(
    val id: String,
    val rating: Int,
    val reviewText: String,
    val isAnonymous: Boolean,
    val createdDateTime: String,
    val author: UserShortModel
)
