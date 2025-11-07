package com.example.moviecatalog.data.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GenreModel(
    val id: String,
    val name: String
) : Parcelable
