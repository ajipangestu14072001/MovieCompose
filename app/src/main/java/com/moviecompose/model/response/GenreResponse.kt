package com.moviecompose.model.response
import com.google.gson.annotations.SerializedName
import com.moviecompose.model.Genre

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)