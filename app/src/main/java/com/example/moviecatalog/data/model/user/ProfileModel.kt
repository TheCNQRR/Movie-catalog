package com.example.moviecatalog.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel(
    val id: String,
    val nickName: String,
    val email: String,
    val avatarLink: String,
    val name: String,
    val birthDate: String,
    val gender: Gender
)
