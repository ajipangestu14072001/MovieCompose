package com.moviecompose.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.moviecompose.model.Genre
import com.moviecompose.model.Movie
import com.moviecompose.ui.theme.Pink
import com.moviecompose.utils.Constants.BASE_BACKDROP_IMAGE_URL
import com.moviecompose.utils.Constants.BASE_POSTER_IMAGE_URL
import com.moviecompose.utils.SectionTitle
import com.moviecompose.view.destinations.ReviewsScreenDestination
import com.moviecompose.viewmodel.DetailsViewModel
import com.moviecompose.viewmodel.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun MovieDetailsScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    currentFilm: Movie,
) {
    var film by remember {
        mutableStateOf(currentFilm)
    }

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    LaunchedEffect(key1 = !film.video) {
        Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show()
    }

    val scope = rememberCoroutineScope()
    val url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    val exoPlayer = ExoPlayer.Builder(context).build()
    val mediaItem = MediaItem.fromUri(Uri.parse(url))
    exoPlayer.setMediaItem(mediaItem)
    val playerView = StyledPlayerView(context)
    playerView.player = exoPlayer

    val similarFilms = detailsViewModel.similarMovies.value.collectAsLazyPagingItems()

    LaunchedEffect(key1 = film) {
        detailsViewModel.getSimilarFilms(filmId = film.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222831))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.33F)
        ) {
            val (
                backdropImage,
                movieTitleBox,
                moviePosterImage,
            ) = createRefs()

            CoilImage(
                imageModel = "$BASE_BACKDROP_IMAGE_URL${film.backdropPath}",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .constrainAs(backdropImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                shimmerParams = ShimmerParams(
                    baseColor = Pink,
                    highlightColor = Pink,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                contentScale = Crop,
                contentDescription = null,
            )


            Column(
                modifier = Modifier.constrainAs(movieTitleBox) {
                    start.linkTo(moviePosterImage.end, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    bottom.linkTo(moviePosterImage.bottom, margin = 10.dp)
                },
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
            ) {


                Text(
                    text = film.title,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .width(200.dp),
                    fontSize = 17.sp,
                    fontWeight = Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 8.dp)
                        .fillMaxWidth(0.42F),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.StarRate,
                        tint = Color(0XFFFDB827),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navigator.navigate(
                                    ReviewsScreenDestination(
                                        filmId = film.id,
                                        filmTitle = film.title
                                    )
                                )
                            },
                        contentDescription = null
                    )
                    IconButton(onClick = {
                        scope.launch {
                            if (sheetState.isVisible) sheetState.hide()
                            else sheetState.show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                }
            }

            CoilImage(
                imageModel = "$BASE_POSTER_IMAGE_URL/${film.posterPath}",
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .width(115.dp)
                    .height(172.5.dp)
                    .constrainAs(moviePosterImage) {
                        top.linkTo(backdropImage.bottom)
                        bottom.linkTo(backdropImage.bottom)
                        start.linkTo(parent.start)
                    },
                shimmerParams = ShimmerParams(
                    baseColor = Pink,
                    highlightColor = Pink,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                contentScale = Crop,
                circularReveal = CircularReveal(duration = 1000),
                contentDescription = null
            )
        }

        LazyRow(
            modifier = Modifier
                .padding(top = (96).dp, bottom = 10.dp, start = 12.dp, end = 4.dp)
                .fillMaxWidth()
        ) {
            val filmGenres: List<Genre> = homeViewModel.filmGenres.filter { genre ->
                return@filter if (film.genreIds.isNullOrEmpty()) false else
                    film.genreIds!!.contains(genre.id)
            }
            filmGenres.forEach { genre ->
                item {
                    MovieGenreChip(
                        background = Color(0XFFF4F4F2),
                        genre = genre.name
                    )
                }
            }
        }


        LazyColumn(
            horizontalAlignment = Alignment.Start
        ) {

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = film.overview,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp,
                )
            }

            item {
                if (similarFilms.itemCount != 0) {
                    SectionTitle(title = "Similar", modifier = Modifier.padding(horizontal = 10.dp))
                }
                Spacer(modifier = Modifier.height(5.dp))
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(similarFilms) { thisMovie ->
                        CoilImage(
                            imageModel = "${BASE_POSTER_IMAGE_URL}/${thisMovie!!.posterPath}",
                            shimmerParams = ShimmerParams(
                                baseColor = Pink,
                                highlightColor = Pink,
                                durationMillis = 500,
                                dropOff = 0.65F,
                                tilt = 20F
                            ),
                            contentScale = Crop,
                            circularReveal = CircularReveal(duration = 1000),
                            modifier = Modifier
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .size(130.dp, 195.dp)
                                .clickable {
                                    film = thisMovie
                                },
                            contentDescription = "Movie item"
                        )
                    }
                }
            }
        }
    }
    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)

            ) {

                Spacer(modifier = Modifier.height(10.dp))

                DisposableEffect(AndroidView(factory = { playerView })) {

                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true

                    onDispose {
                        exoPlayer.release()
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    ) {}
}

@Composable
fun MovieGenreChip(
    genre: String,
    background: Color,
) {
    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .widthIn(min = 80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Pink,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}


