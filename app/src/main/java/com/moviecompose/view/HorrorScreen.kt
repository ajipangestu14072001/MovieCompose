package com.moviecompose.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.moviecompose.model.Genre
import com.moviecompose.ui.theme.Pink
import com.moviecompose.utils.Constants
import com.moviecompose.view.destinations.MovieDetailsScreenDestination
import com.moviecompose.viewmodel.HomeViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun HorrorScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val trendingFilms = homeViewModel.trendingMoviesState.value.collectAsLazyPagingItems()

    LaunchedEffect(Unit){
        homeViewModel.filterBySetSelectedGenre(genre = Genre(id = 27, name = "Horror"))
    }
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(trendingFilms.itemCount) { index ->
                val film = trendingFilms[index]

                film?.let {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                            .clickable {
                                navigator.navigate(
                                    direction = MovieDetailsScreenDestination(film)
                                ) {
                                    launchSingleTop = true
                                }
                            }
                    ) {
                        CoilImage(
                            imageModel = "${Constants.BASE_POSTER_IMAGE_URL}/${it.posterPath}",
                            shimmerParams = ShimmerParams(
                                baseColor = Pink,
                                highlightColor = Color.Gray,
                                durationMillis = 500,
                                dropOff = 0.65F,
                                tilt = 20F
                            ),
                            contentScale = ContentScale.Crop,
                            circularReveal = CircularReveal(duration = 1000),
                            contentDescription = "Movie item"
                        )
                    }
                }
            }
        }
    
}
