package com.example.moviecatalog.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String,
)
