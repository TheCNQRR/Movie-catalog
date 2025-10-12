package com.example.moviecatalog.data.model.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewModifyModel(
    val reviewText: String,
    val rating: Int?, //TODO проверка на >=0 и <=10
    val isAnonymous: Boolean?
)
