package com.example.moviecatalog.data.model.review

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ReviewShortModel(
    val id: String,
    val rating: Int
) : Parcelable
