package com.moviecompose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.moviecompose.model.Genre
import com.moviecompose.utils.Chip
import com.moviecompose.utils.ScrollableMovieItems
import com.moviecompose.utils.SectionTitle
import com.moviecompose.utils.ShowAboutCategory
import com.moviecompose.viewmodel.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222831))
    ) {
        NestedScroll(navigator = navigator, homeViewModel)
    }
}


@Composable
fun NestedScroll(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel,
) {
    val trendingFilms = homeViewModel.trendingMoviesState.value.collectAsLazyPagingItems()
    val popularFilms = homeViewModel.popularFilmsState.value.collectAsLazyPagingItems()
    val topRatedFilms = homeViewModel.topRatedFilmState.value.collectAsLazyPagingItems()
    val nowPlayingFilms = homeViewModel.nowPlayingMoviesState.value.collectAsLazyPagingItems()
    val upcomingMovies = homeViewModel.upcomingMoviesState.value.collectAsLazyPagingItems()
    val recommendedFilms = homeViewModel.recommendedMovies.value.collectAsLazyPagingItems()
    val listState: LazyListState = rememberLazyListState()

    LaunchedEffect(Unit){
        homeViewModel.filterBySetSelectedGenre(genre = Genre(id = null, name = "All"))
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .fillMaxSize()
    ) {
        item {
            val genres = homeViewModel.filmGenres
            LazyRow(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                items(count = genres.size) {
                    Chip(
                        genre = genres[it].name,
                        selected = genres[it].name == homeViewModel.selectedGenre.value.name,
                        onClick = {
                            if (homeViewModel.selectedGenre.value.name != genres[it].name) {
                                homeViewModel.selectedGenre.value = genres[it]
                                homeViewModel.filterBySetSelectedGenre(genre = genres[it])
                            }
                        }
                    )
                }
            }
        }

        item {
            SectionTitle(title = "Trending")
            ScrollableMovieItems(
                landscape = true,
                navigator = navigator,
                pagingItems = trendingFilms,
                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }
        item {
            SectionTitle(title = "Popular")
            ScrollableMovieItems(
                navigator = navigator,
                pagingItems = popularFilms,
                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }

        item {
            SectionTitle(title = "Top Rated")
            ScrollableMovieItems(
                navigator = navigator,
                pagingItems = topRatedFilms,
                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }

        item {
            SectionTitle(title = "Now Playing")
            ScrollableMovieItems(
                navigator = navigator,
                pagingItems = nowPlayingFilms,
                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }
            item {
                SectionTitle(title = "Upcoming")
                ScrollableMovieItems(
                    navigator = navigator,
                    pagingItems = upcomingMovies,
                    onErrorClick = {
                        homeViewModel.refreshAll()
                    }
                )
            }


        if (recommendedFilms.itemCount != 0) {
            item {
                ShowAboutCategory(
                    name = "For You",
                    description = "Recommendation based on your watchlist"
                )
            }

            item {
                ScrollableMovieItems(
                    navigator = navigator,
                    pagingItems = recommendedFilms,
                    onErrorClick = {
                        homeViewModel.refreshAll()
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}




