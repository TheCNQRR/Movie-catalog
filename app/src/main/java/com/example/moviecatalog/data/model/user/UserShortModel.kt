package com.example.moviecatalog.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserShortModel(
    val userId: String,
    val nickName: String? = null,
    val avatar: String? = null
)
