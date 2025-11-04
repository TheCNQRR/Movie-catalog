package com.example.moviecatalog.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.example.moviecatalog.databinding.MovieItemInGalleryBinding
import com.example.moviecatalog.logic.util.Functions
import com.squareup.picasso.Picasso
import java.util.Locale

class GalleryAdapter(
    private var movies: List<MovieElementModel> = emptyList(),
    private val onMovieClick: (MovieElementModel) -> Unit = {}
): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    class GalleryViewHolder(private val binding: MovieItemInGalleryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieElementModel, onMovieClick: (MovieElementModel) -> Unit) = with(binding) {
            val movieRating = Functions().calculateMovieRating(movie)

            val ratingBackground = when {
                movieRating > 9 && movieRating <= 10 -> R.drawable.rating_masterprice
                movieRating > 8 && movieRating <= 9 -> R.drawable.rating_excellent
                movieRating > 7 && movieRating <= 8 -> R.drawable.rating_very_good
                movieRating > 6 && movieRating <= 7-> R.drawable.rating_good
                movieRating > 5 && movieRating <= 6 -> R.drawable.rating_decent
                movieRating > 4 && movieRating <= 5 -> R.drawable.rating_average
                movieRating > 3 &&  movieRating <= 4 -> R.drawable.rating_below_average
                movieRating > 2 && movieRating <= 3 -> R.drawable.rating_poor
                movieRating > 1 && movieRating <= 2 -> R.drawable.rating_very_poor
                movieRating in 0.0..1.0 -> R.drawable.rating_awful
                else -> R.drawable.rating_gray
            }

            movieNameMainScreen.isSelected = true
            movieYearCountryMainScreen.isSelected = true
            movieGenresMainScreen.isSelected = true

            Picasso.get().load(movie.poster).into(moviePoster)
            movieNameMainScreen.text = movie.name
            movieYearCountryMainScreen.text = root.context.getString(R.string.year_country_format, movie.year, movie.country)
            movieGenresMainScreen.text = movie.genres.joinToString { it.name }
            movieRatingMainScreen.text = String.format(Locale.getDefault(), "%.1f", movieRating)
            movieRatingMainScreen.setBackgroundResource(ratingBackground)

            cardView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = MovieItemInGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(movies[position], onMovieClick)
    }

    fun addMovies(newMovies: List<MovieElementModel>) {
        val startPosition = movies.size
        movies = movies + newMovies
        notifyItemRangeInserted(startPosition, newMovies.size)
    }
}