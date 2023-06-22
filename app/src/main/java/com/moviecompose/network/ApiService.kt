package com.moviecompose.network

import com.moviecompose.model.response.FilmResponse
import com.moviecompose.model.response.GenreResponse
import com.moviecompose.model.response.MultiSearchResponse
import com.moviecompose.model.response.ReviewsResponse
import com.moviecompose.utils.Constants.API_KEY
import com.moviecompose.utils.Constants.GENRE
import com.moviecompose.utils.Constants.HEADER
import com.moviecompose.utils.Constants.MULTI_LIST
import com.moviecompose.utils.Constants.NOW_PLAYING
import com.moviecompose.utils.Constants.POPULAR
import com.moviecompose.utils.Constants.REVIEW
import com.moviecompose.utils.Constants.SIMILAR
import com.moviecompose.utils.Constants.TOP_RATE
import com.moviecompose.utils.Constants.TRENDING
import com.moviecompose.utils.Constants.UPCOMING
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(TRENDING)
    suspend fun getTrendingMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET(POPULAR)
    suspend fun getPopularMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET(TOP_RATE)
    suspend fun getTopRatedMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET(NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET(UPCOMING)
    suspend fun getUpcomingMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse


    @GET(SIMILAR)
    suspend fun getSimilarMovies(
        @Header(HEADER) auth: String = API_KEY,
        @Path("movie_id") filmId: Int,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET(GENRE)
    suspend fun getMovieGenres(
        @Header(HEADER) auth: String = API_KEY,
        @Query("language") language: String = "en"
    ): GenreResponse

    @GET(MULTI_LIST)
    suspend fun multiSearch(
        @Header(HEADER) auth: String = API_KEY,
        @Query("query") searchParams: String,
        @Query("page") page: Int = 0,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("language") language: String = "en"
    ): MultiSearchResponse

    @GET(REVIEW)
    suspend fun getMovieReviews(
        @Header(HEADER) auth: String = API_KEY,
        @Path("film_id") filmId: Int,
        @Path("film_path") filmPath: String,
        @Query("page") page: Int = 0,
        @Query("language") language: String = "en-US"
    ): ReviewsResponse


}
