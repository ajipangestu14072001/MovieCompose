package com.moviecompose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.moviecompose.model.response.Review
import com.moviecompose.utils.Constants
import com.moviecompose.viewmodel.ReviewsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

@Destination
@Composable
fun ReviewsScreen(
    navigator: DestinationsNavigator,
    reviewsViewModel: ReviewsViewModel = hiltViewModel(),
    filmId: Int,
    filmTitle: String?
) {
    val reviews = reviewsViewModel.filmReviews.value.collectAsLazyPagingItems()

    LaunchedEffect(key1 = filmId) {
        reviewsViewModel.getFilmReview(filmId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222831))
    ) {

        Text(
            text = filmTitle ?: "",
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = Color.White
        )

        LazyColumn {
            items(reviews) { review ->
                ReviewItem(item = review)
            }
        }
    }
}

@Composable
fun ReviewItem(item: Review?) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        Row(horizontalArrangement = Arrangement.Center) {
            CoilImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                imageModel = "${Constants.BASE_POSTER_IMAGE_URL}${item?.authorDetails?.avatarPath}",
                shimmerParams = ShimmerParams(
                    baseColor = Color.LightGray,
                    highlightColor = Color.White,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                contentScale = ContentScale.Crop,
                circularReveal = CircularReveal(duration = 1000),
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {

                item?.authorDetails?.name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Text(
                    text = "@${item?.authorDetails?.userName}",
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF6F6F6))
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            Column(verticalArrangement = SpaceBetween) {
                SelectionContainer {
                    Text(
                        text = item?.content ?: "Review not found!",
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = SpaceBetween
                ) {
                    RatingBar(
                        value = (item?.authorDetails?.rating?.div(2))?.toFloat() ?: 0.toFloat(),
                        config = RatingBarConfig()
                            .style(RatingBarStyle.Normal)
                            .isIndicator(true)
                            .activeColor(Color(0XFFFDB827))
                            .hideInactiveStars(false)
                            .inactiveColor(Color.LightGray.copy(alpha = 0.3F))
                            .stepSize(StepSize.HALF)
                            .numStars(5)
                            .size(16.dp)
                            .padding(4.dp),
                        onValueChange = {},
                        onRatingChanged = {}
                    )

                    Text(
                        text = item?.createdOn?.removeRange(10..item.createdOn.lastIndex) ?: "",
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
