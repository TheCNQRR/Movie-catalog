package com.example.moviecatalog.data.model.review

import android.os.Parcelable
import com.example.moviecatalog.data.model.user.UserShortModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ReviewModel(
    val id: String,
    val rating: Int,
    val reviewText: String,
    val isAnonymous: Boolean,
    val createDateTime: String?,
    val author: UserShortModel?
): Parcelable
