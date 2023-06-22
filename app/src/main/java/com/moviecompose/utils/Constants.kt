package com.moviecompose.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val HEADER = "Authorization"
    const val API_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzI0OGU1ODI0OGQxZDhhOThjMDBlODY4ZmIxOTBkZSIsInN1YiI6IjYzZmQ4NDI2ZGY4NmE4MDA3YjU3OWEyMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.PqfnSrMsLExroOB67x4Dax4dJHgfR_Q8X1J4tAy7g7Y"
    const val GENRE = "genre/movie/list"
    const val MULTI_LIST = "search/multi"
    const val SIMILAR = "movie/{movie_id}/similar"
    const val REVIEW = "{film_path}/{film_id}/reviews?"
    const val NOW_PLAYING = "movie/now_playing"
    const val TOP_RATE = "movie/top_rated"
    const val POPULAR = "movie/popular"
    const val TRENDING = "trending/movie/day"
    const val UPCOMING = "movie/upcoming"
    const val BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
    const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
}