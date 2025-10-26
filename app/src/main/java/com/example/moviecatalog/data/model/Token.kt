package com.example.moviecatalog.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("token")
    var accessToken: String,
    val expiresIn: Long? = null
)
