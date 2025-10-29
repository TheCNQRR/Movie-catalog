package com.example.moviecatalog.logic.util

import com.example.moviecatalog.data.model.movie.MovieElementModel

class Functions {
    fun calculateMovieRating(movie: MovieElementModel): Double {
        val reviews = movie.reviews
        if (reviews.isEmpty()) {
            return  0.0
        }

        val rating: Double
        var reviewsSum = 0

        reviews.forEach { review ->
            reviewsSum += review.rating
        }

        rating = reviewsSum.toDouble() / reviews.size

        return rating
    }
}