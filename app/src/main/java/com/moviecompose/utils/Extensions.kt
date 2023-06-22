package com.moviecompose.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.moviecompose.R
import com.moviecompose.model.Genre
import com.moviecompose.model.Movie
import com.moviecompose.ui.theme.Pink
import com.moviecompose.view.MovieGenreChip
import com.moviecompose.view.destinations.MovieDetailsScreenDestination
import com.moviecompose.viewmodel.SearchViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

fun Modifier.icon() = this.size(24.dp)

val bottomBarHeight = 60.dp

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(start = 4.dp, top = 8.dp)
    )
}

@Composable
fun Chip(
    genre: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = if (selected) Pink else Color.White,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50.dp),
        backgroundColor = if (selected) Color.White else Pink,
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 6.dp),
            text = genre,
            color = if (selected) Pink else Color.White,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun ShowAboutCategory(name: String, description: String) {
    var showAboutThisCategory by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            fontSize = 24.sp,
            color = Pink,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = 4.dp, top = 14.dp,
                end = 8.dp, bottom = 4.dp
            )
        )
        IconButton(
            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp),
            onClick = { showAboutThisCategory = showAboutThisCategory.not() }) {
            Icon(
                imageVector = if (showAboutThisCategory) Icons.Filled.KeyboardArrowUp else Icons.Filled.Info,
                tint = Pink,
                contentDescription = "Info Icon"
            )
        }
    }

    AnimatedVisibility(visible = showAboutThisCategory) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .border(
                    width = 1.dp, color = Pink,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(Pink.copy(alpha = 0.25F))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = description,
                    color = Pink
                )
            }
        }
    }
}

@Composable
fun ScrollableMovieItems(
    landscape: Boolean = false,
    navigator: DestinationsNavigator,
    pagingItems: LazyPagingItems<Movie>,
    onErrorClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (!landscape) 215.dp else 195.dp)
    ) {
        when (pagingItems.loadState.refresh) {

            is LoadState.NotLoading -> {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(pagingItems) { film ->
                        val imagePath =
                            if (landscape) "${Constants.BASE_BACKDROP_IMAGE_URL}/${film!!.backdropPath}"
                            else "${Constants.BASE_POSTER_IMAGE_URL}/${film!!.posterPath}"

                        MovieItem(
                            imageUrl = imagePath,
                            modifier = Modifier
                                .width(if (landscape) 215.dp else 130.dp)
                                .height(if (landscape) 161.25.dp else 195.dp)
                        ) {
                            navigator.navigate(
                                direction = MovieDetailsScreenDestination(film)
                            ) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
            is LoadState.Error -> {
                val error = pagingItems.loadState.refresh as LoadState.Error
                val errorMessage = when (error.error) {
                    is HttpException -> "Sorry, Something went wrong!\nTap to retry"
                    is IOException -> "Connection failed. Tap to retry!"
                    else -> "Failed! Tap to retry!"
                }
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(161.25.dp)
                        .clickable {
                            onErrorClick()
                        }
                ) {
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFFE28B8B),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            else -> {
            }
        }
    }
}

@Composable
fun MovieItem(
    imageUrl: String,
    modifier: Modifier,
    onclick: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(all = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onclick()
            },
        horizontalAlignment = Alignment.Start
    ) {
        CoilImage(
            imageModel = imageUrl,
            shimmerParams = ShimmerParams(
                baseColor = Pink,
                highlightColor = Color.Gray,
                durationMillis = 500,
                dropOff = 0.65F,
                tilt = 20F
            ),
            failure = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {

                }
            },
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 1000),
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
            contentDescription = "Movie item"
        )
    }
}

@Composable
fun SearchResultItem(
    title: String?,
    mediaType: String?,
    posterImage: String?,
    genres: List<Genre>?,
    rating: Double,
    releaseYear: String?,
    onClick: () -> Unit?
) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Pink)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                imageModel = posterImage,
                circularReveal = CircularReveal(duration = 1000),
                shimmerParams = ShimmerParams(
                    baseColor = Pink,
                    highlightColor = Pink,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(86.67.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxSize()
            ) {
                var paddingValue by remember { mutableStateOf(2) }
                Text(
                    text = when (mediaType) {
                        "movie" -> {
                            paddingValue = 2
                            "Movie"
                        }
                        else -> {
                            paddingValue = 0
                            ""
                        }
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(size = 4.dp))
                        .background(Color.LightGray.copy(alpha = 0.2F))
                        .padding(paddingValue.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                )
                Text(
                    text = title ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Text(
                    text = releaseYear ?: "",
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.White
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RatingBar(
                        value = rating.toFloat() / 2,
                        modifier = Modifier.padding(end = 6.dp, bottom = 10.dp),
                        config = RatingBarConfig()
                            .style(RatingBarStyle.Normal)
                            .isIndicator(true)
                            .activeColor(Color(0XFFFDB827))
                            .hideInactiveStars(false)
                            .inactiveColor(Color.LightGray.copy(alpha = 0.2F))
                            .stepSize(StepSize.HALF)
                            .numStars(5)
                            .size(16.dp)
                            .padding(4.dp),
                        onValueChange = {},
                        onRatingChanged = {}
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genres?.forEach {
                        item {
                            MovieGenreChip(
                                background = Color(0XFFF4F4F2),
                                genre = it.name,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    autoFocus: Boolean,
    viewModel: SearchViewModel = hiltViewModel(),
    onSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .clip(CircleShape)
            .background(Color.White)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        var searchInput: String by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(key1 = searchInput) {
            if (viewModel.searchParam.value.trim().isNotEmpty() &&
                viewModel.searchParam.value.trim().length != viewModel.previousSearch.value.length
            ) {
                delay(750)
                onSearch()
                viewModel.previousSearch.value = searchInput.trim()
            }
        }

        TextField(
            value = searchInput,
            onValueChange = { newValue ->
                searchInput = if (newValue.trim().isNotEmpty()) newValue else ""
                viewModel.searchParam.value = searchInput
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester = focusRequester),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search...",
                    color = Color.Gray
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                textColor = Pink,
                backgroundColor = Color.Transparent,
                disabledTextColor = Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ), keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (viewModel.searchParam.value.trim().isNotEmpty()) {
                        focusManager.clearFocus()
                        viewModel.searchParam.value = searchInput
                        if (searchInput != viewModel.previousSearch.value) {
                            viewModel.previousSearch.value = searchInput
                            onSearch()
                        }
                    }
                }
            ),
            trailingIcon = {
                LaunchedEffect(Unit) {
                    if (autoFocus) {
                        focusRequester.requestFocus()
                    }
                }
                Row {
                    AnimatedVisibility(visible = searchInput.trim().isNotEmpty()) {
                        IconButton(onClick = {

                            focusManager.clearFocus()
                            searchInput = ""
                            viewModel.searchParam.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = Pink,
                                contentDescription = null
                            )
                        }
                    }
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    tint = Pink,
                    contentDescription = null
                )
            }
        )
    }
}
