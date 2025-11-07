package com.example.moviecatalog.ui

import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.model.movie.MovieDetailsModel
import com.example.moviecatalog.data.model.review.ReviewModel
import com.example.moviecatalog.data.model.user.ProfileModel
import com.squareup.picasso.Picasso
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun MovieScreen(movie: MovieDetailsModel,
                user: ProfileModel,
                onBackButtonClick: () -> Unit,
                onAddReview: (String, Int, String, Boolean) -> Unit,
                onDeleteReview: (String, String) -> Unit,
                onEditReview: (String, String, Int, String, Boolean) -> Unit) {
    val scrollState = rememberScrollState()
    val progress = (scrollState.value / 150f).coerceIn(0f, 1f)
    val showDialog = remember { mutableStateOf(false) }
    val editingReview = remember { mutableStateOf<ReviewModel?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background))
                .verticalScroll(scrollState)
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
                        .background(Color.Black.copy(alpha = progress * 0.7f))
                )

                Text(
                    text = movie.name,
                    fontSize = (36 * (1 - progress * 0.5f)).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 1f - progress),
                    maxLines = if (progress > 0.5f) 1 else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
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

            val expanded = remember { mutableStateOf(false) }

            Button(
                onClick = {
                    expanded.value = true
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

//            DropdownMenu(
//                expanded = expanded.value,
//                onDismissRequest = { expanded.value = false },
//                modifier = Modifier.background(Color.Black)
//            ) {
//                collectionsList.forEach { collection ->
//                    DropdownMenuItem(
//                        text = {
//                            Text(
//                                text = collection.,
//                                color = Color.White,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        },
//                        onClick = {
//                            //TODO обработать выбор
//                            expanded.value = false
//                        }
//                    )
//                }
//            }
            }

            AboutMovie(movie)
            MovieGenres(movie)
            Reviews(
                movie,
                user,
                onShowDialog = { showDialog.value = true },
                onDeleteReview = onDeleteReview,
                onEditReview = { review ->
                    editingReview.value = review
                    showDialog.value = true
                })
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = colorResource(R.color.background).copy(alpha = progress)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = onBackButtonClick,
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = colorResource(R.color.toolbar_background).copy(alpha = progress),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.toolbar_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = movie.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = progress),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 9.dp,
                        end = 8.dp
                    )
                    .alpha(progress)
            )

            IconButton(
                onClick = {
                    //TODO добавление фильма в избранное
                },
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = colorResource(R.color.toolbar_background).copy(alpha = progress),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.toolbar_add_to_favorite),
                    contentDescription = "Favorite",
                    tint = colorResource(R.color.accent)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
        }
        if (showDialog.value) {
            ReviewDialog(
                movie,
                editingReview = editingReview.value,
                onAddReview = onAddReview,
                onEditReview = onEditReview,
                onDismiss = {
                    showDialog.value = false
                    editingReview.value = null
                }
            )
        }
    }
}

@Composable
fun PicassoImage(
    url: String,
    modifier: Modifier = Modifier,
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = stringResource(R.string.genres),
            fontSize = 16.sp,
            color = colorResource(R.color.white)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp
                ),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = Int.MAX_VALUE
        ) {
            movie.genres.forEachIndexed { _, genre ->
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                           end = 8.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.accent),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 6.dp
                            )
                    ) {
                        Text(
                            text = genre.name,
                            fontSize = 12.sp,
                            color = colorResource(R.color.white)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Reviews(
    movie: MovieDetailsModel,
    user: ProfileModel,
    onShowDialog: () -> Unit,
    onDeleteReview: (String, String) -> Unit,
    onEditReview: (ReviewModel) -> Unit
) {
    val isUserHasReview = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize(),
                text = stringResource(R.string.reviews),
                fontSize = 16.sp,
                color = colorResource(R.color.white)
            )

            movie.reviews.forEach { review ->
                if (review.author!!.userId == user.id) {
                    isUserHasReview.value = true
                }
            }

            if (!isUserHasReview.value) {
                IconButton(
                    onClick = onShowDialog,
                    modifier = Modifier
                        .size(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_review),
                        contentDescription = stringResource(R.string.add_review),
                        tint = colorResource(R.color.accent)
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        movie.reviews.forEach { review ->
            Review(
                movie,
                review,
                user,
                onDeleteReview = onDeleteReview,
                onEditReview = onEditReview
            )
        }
    }
}

@Composable
fun Review(
    movie: MovieDetailsModel,
    review: ReviewModel,
    user: ProfileModel,
    onDeleteReview: (String, String) -> Unit,
    onEditReview: (ReviewModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp
            )
            .background(
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.background)

            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.gray)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(
                        end = 8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .padding(
                                start = 8.dp
                            )
                    ) {
                        val avatarUrl = review.author!!.avatar
                        if (!avatarUrl.isNullOrBlank() && avatarUrl != "" && !review.isAnonymous) {
                            PicassoImage(
                                url = avatarUrl,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                        else {
                            Icon(
                                painter = painterResource(R.drawable.default_user_icon),
                                contentDescription = "Default avatar",
                                modifier = Modifier
                                    .size(40.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(40.dp)
                            .padding(
                                start = 8.dp
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (review.isAnonymous) stringResource(R.string.anonymous_user) else review.author!!.nickName!!,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.white)
                        )
                        if (!review.isAnonymous && review.author!!.userId == user.id) {
                            Text(
                                text = stringResource(R.string.my_review),
                                fontSize = 12.sp,
                                color = colorResource(R.color.gray)
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(28.dp)
                        .background(
                            color = colorResource(R.color.accent),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.rating.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(R.color.white)
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 8.dp,
                        top = 8.dp
                    ),
                text = review.reviewText,
                fontSize = 14.sp,
                color = colorResource(R.color.white)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 8.dp
                    ),
                horizontalArrangement = if (review.createdDateTime != null) Arrangement.SpaceBetween else Arrangement.End
            ) {
                if (review.createdDateTime != null) {
                    Text(
                        text = review.createdDateTime,
                        fontSize = 12.sp,
                        color = colorResource(R.color.gray)
                    )
                }
                if (review.author!!.userId == user.id) {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                end = 8.dp
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = colorResource(R.color.edit_review_background),
                                    shape = CircleShape
                                )
                                .clickable {
                                    onEditReview(review)
                                           },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit_review),
                                contentDescription = "Edit review",
                                modifier = Modifier
                                    .size(8.dp),
                                tint = colorResource(R.color.gray_faded)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = colorResource(R.color.delete_review_background),
                                    shape = CircleShape
                                )
                                .clickable {
                                onDeleteReview(movie.id, review.id)
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete_review),
                                contentDescription = "Delete review",
                                modifier = Modifier
                                    .size(8.dp),
                                tint = colorResource(R.color.gray_faded)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewDialog(
    movie: MovieDetailsModel,
    editingReview: ReviewModel?,
    onAddReview: (String, Int, String, Boolean) -> Unit,
    onEditReview: (String, String, Int, String, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val reviewText = remember { mutableStateOf("") }
    val checkIcon = remember { mutableStateOf(false) }
    val rating = remember { mutableStateOf(0) }
    val showError = remember { mutableStateOf(false) }

    LaunchedEffect(editingReview) {
        reviewText.value = editingReview?.reviewText ?: ""
        checkIcon.value = editingReview?.isAnonymous ?: false
        rating.value = editingReview?.rating ?: 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colorResource(R.color.background).copy(alpha = 0.8f)
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .width(328.dp)
                .height(383.dp)
                .background(
                    color = colorResource(R.color.review_background),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
        ) {
            Text(
                text = stringResource(R.string.leave_a_review),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(10) { index ->
                    val starIndex = index + 1

                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = {
                            rating.value = starIndex
                        }
                    ) {
                        val iconStar = if (starIndex <= rating.value) {
                            painterResource(R.drawable.star_filled)
                        }
                        else {
                            painterResource(R.drawable.rating_star)
                        }

                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (starIndex <= rating.value) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            color = colorResource(R.color.accent).copy(alpha = 0.1f),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        Icon(
                            painter = iconStar,
                            contentDescription = "Star",
                            modifier = Modifier.size(18.dp),
                            tint = if (starIndex <= rating.value) {
                                colorResource(R.color.accent)
                            }
                            else {
                                colorResource(R.color.gray_faded)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = reviewText.value,
                onValueChange = { newText -> reviewText.value = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.review),
                        fontSize = 14.sp,
                        color = colorResource(R.color.gray_faded)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white),
                    disabledContainerColor = colorResource(R.color.white),
                    focusedTextColor = colorResource(R.color.background),
                    unfocusedTextColor = colorResource(R.color.background),
                    cursorColor = colorResource(R.color.background),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp
                ),
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                shape = RoundedCornerShape(6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.anonymous_review),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.gray_faded)
                )

                Box(
                    modifier = Modifier
                        .border(
                            color = colorResource(R.color.check_box),
                            shape = RoundedCornerShape(4.dp),
                            width = 1.dp
                        )
                        .size(24.dp)
                        .clickable { checkIcon.value = !checkIcon.value },
                    contentAlignment = Alignment.Center
                ) {
                    if (checkIcon.value) {
                        Icon(
                            painter = painterResource(R.drawable.check_icon),
                            contentDescription = "Check icon",
                            modifier = Modifier
                                .width(15.dp)
                                .height(10.dp),
                            tint = colorResource(R.color.accent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (reviewText.value.isNotBlank()) {
                        if (editingReview != null) {
                            onEditReview(movie.id, editingReview.id, rating.value, reviewText.value, checkIcon.value)
                        }
                        else {
                            onAddReview(movie.id, rating.value, reviewText.value, checkIcon.value)
                        }
                        onDismiss()
                    }
                    else {
                        showError.value = true
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.accent)
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onDismiss()
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.review_background)
                )
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.accent),
                    textAlign = TextAlign.Center
                )
            }
        }
        if (showError.value) {
            ErrorNotification(
                message = stringResource(R.string.fill_all_fields),
                onDismiss = { showError.value = false }
            )
        }
    }
}

@Composable
fun ErrorNotification(
    message: String,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000)
        visible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
    ) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(color = colorResource(R.color.error))
                    .padding(24.dp)
                    .wrapContentSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = colorResource(R.color.white))
                            .clickable { onDismiss() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.error),
                            tint = colorResource(R.color.error),
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Text(
                        text = message,
                        style = TextStyle(
                            color = colorResource(R.color.white),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}