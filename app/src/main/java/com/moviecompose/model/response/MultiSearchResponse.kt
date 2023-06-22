package com.moviecompose.model.response
import com.google.gson.annotations.SerializedName
import com.moviecompose.model.Search

class MultiSearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Search>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
