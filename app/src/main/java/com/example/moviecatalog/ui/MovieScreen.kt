package com.example.moviecatalog.ui

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.movie.MovieElementModel
import com.squareup.picasso.Picasso

@Composable
fun MovieScreen(movie: MovieDetailsModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            PicassoImage(
                url = movie.poster,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Text(
                text = movie.name,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .width(343.dp)
                    .wrapContentHeight()
                    .padding(
                        start = 16.dp,
                        top = 154.dp
                    )
            )
        }

        Text(
            text = movie.description,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .width(343.dp)
                .wrapContentHeight()
                .padding(
                    start = 16.dp,
                    top = 16.dp
                )
        )

        Button(
            onClick = {
            //TODO добавить в коллекцию
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                    ),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.accent)
            )
        ) {
            Text(
                text = stringResource(R.string.add_to_collection),
                fontSize = 16.sp,
                color = colorResource(R.color.white),
                textAlign = TextAlign.Center
            )
        }

        AboutMovie(movie)
        MovieGenres(movie)
        Reviews(movie)
    }
}

@Composable
fun PicassoImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            Picasso.get()
                .load(url)
                .fit()
                .into(imageView)
        },
        modifier = modifier
    )
}

@Composable
fun AboutMovie(movie: MovieDetailsModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Text(
            text = stringResource(R.string.about_movie),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            fontSize = 16.sp,
            color = colorResource(R.color.white)
        )

        val fieldPairs = listOf(
            Pair(stringResource(R.string.year), movie.year.toString()),
            Pair(stringResource(R.string.country), movie.country),
            Pair(stringResource(R.string.time), "${movie.time} мин."),
            Pair(stringResource(R.string.tagline), "\"${movie.tagline}\""),
            Pair(stringResource(R.string.director), movie.director),
            Pair(stringResource(R.string.budget), formatNumber(movie.budget)),
            Pair(stringResource(R.string.fees), formatNumber(movie.fees)),
            Pair(stringResource(R.string.age), "${movie.ageLimit}+")
        )

        val maxWidth = 100.dp

        fieldPairs.forEachIndexed { index, (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (index == 0) 8.dp else 4.dp,
                        bottom = 4.dp
                    )
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = colorResource(R.color.about_movie),
                    modifier = Modifier.width(maxWidth),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = value,
                    fontSize = 12.sp,
                    color = colorResource(R.color.white),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

private fun formatNumber(number: Int): String {
    val numberString = number.toString()
    val result = StringBuilder()
    var count = 0

    for (i in numberString.length - 1 downTo 0) {
        if (count > 0 && count % 3 == 0) {
            result.append(' ')
        }
        result.append(numberString[i])
        count++
    }

    return "$${result.reverse()}"
}

@Composable
fun MovieGenres(movie: MovieDetailsModel) {

}

@Composable
fun Reviews(movie: MovieDetailsModel) {

}

@Composable
fun ReviewDialog() {

}