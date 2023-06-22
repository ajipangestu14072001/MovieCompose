package com.moviecompose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.moviecompose.model.Movie
import com.moviecompose.ui.theme.Pink
import com.moviecompose.utils.Constants.BASE_POSTER_IMAGE_URL
import com.moviecompose.utils.SearchBar
import com.moviecompose.utils.SearchResultItem
import com.moviecompose.view.destinations.MovieDetailsScreenDestination
import com.moviecompose.viewmodel.HomeViewModel
import com.moviecompose.viewmodel.SearchViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    searchViewModel: SearchViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val searchResult = searchViewModel.multiSearchState.value.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Pink)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        SearchBar(
            autoFocus = true,
            onSearch = {
                searchViewModel.searchRemoteMovie( true)
            })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            when (searchResult.loadState.refresh) {
                is LoadState.NotLoading -> {
                    items(searchResult) { film ->
                        val focus = LocalFocusManager.current
                        SearchResultItem(
                            title = film!!.title,
                            mediaType = film.mediaType,
                            posterImage = "$BASE_POSTER_IMAGE_URL/${film.posterPath}",
                            genres = homeViewModel.filmGenres.filter { genre ->
                                return@filter if (film.genreIds.isNullOrEmpty()) false else
                                    film.genreIds.contains(genre.id)
                            },
                            rating = (film.voteAverage ?: 0) as Double,
                            releaseYear = film.releaseDate,
                            onClick = {
                                val navFilm = Movie(
                                    adult = film.adult ?: false,
                                    backdropPath = film.backdropPath,
                                    posterPath = film.posterPath,
                                    genreIds = film.genreIds,
                                    genres = film.genres,
                                    mediaType = film.mediaType,
                                    id = film.id ?: 0,
                                    imdbId = film.imdbId,
                                    originalLanguage = film.originalLanguage ?: "",
                                    overview = film.overview ?: "",
                                    popularity = film.popularity ?: 0F.toDouble(),
                                    releaseDate = film.releaseDate ?: "",
                                    runtime = film.runtime,
                                    title = film.title ?: "",
                                    video = film.video ?: false,
                                    voteAverage = film.voteAverage ?: 0F.toDouble(),
                                    voteCount = film.voteCount ?: 0
                                )
                                focus.clearFocus()
                                navigator.navigate(
                                    direction = MovieDetailsScreenDestination(
                                        navFilm
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            })
                    }
                    if (searchResult.itemCount == 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No Movie Found")
                            }

                        }
                    }
                }

                is LoadState.Loading -> item {
                    if (searchViewModel.searchParam.value.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Movie Found")
                    }
                }
            }
        }
    }
}