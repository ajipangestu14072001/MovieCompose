package com.moviecompose.repository
import com.moviecompose.paging.TrendingFilmSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moviecompose.model.Movie
import com.moviecompose.network.ApiService
import com.moviecompose.paging.NowPlayingFilmSource
import com.moviecompose.paging.PopularFilmSource
import com.moviecompose.paging.SimilarFilmSource
import com.moviecompose.paging.TopRatedFilmSource
import com.moviecompose.paging.UpcomingFilmSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val api: ApiService
) {
    fun getTrendingFilms(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                TrendingFilmSource(api = api)
            }
        ).flow
    }

    fun getPopularFilms(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                PopularFilmSource(api = api)
            }
        ).flow
    }

    fun getTopRatedFilm(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                TopRatedFilmSource(api = api)
            }
        ).flow
    }

    fun getNowPlayingFilms(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                NowPlayingFilmSource(api = api)
            }
        ).flow
    }

    fun getUpcomingTvShows(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                UpcomingFilmSource(api = api)
            }
        ).flow
    }


    fun getSimilarFilms(movieId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                SimilarFilmSource(api = api, filmId = movieId)
            }
        ).flow
    }


}
