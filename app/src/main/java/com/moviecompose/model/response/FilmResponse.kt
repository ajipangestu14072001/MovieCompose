package com.moviecompose.model.response
import com.google.gson.annotations.SerializedName
import com.moviecompose.model.Movie

data class FilmResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)