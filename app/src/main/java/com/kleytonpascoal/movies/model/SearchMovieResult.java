package com.kleytonpascoal.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kleyton on 12/05/17.
 */

public class SearchMovieResult {

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<MovieShortInfo> results;

    @SerializedName("total_results")
    public int totalResults;

    @SerializedName("total_pages")
    public int totalPages;

}
