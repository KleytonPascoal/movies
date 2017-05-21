package com.kleytonpascoal.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kleyton on 12/05/17.
 */

public class SearchMovieListResult {

    @SerializedName("Response")
    public boolean response;

    @SerializedName("totalResults")
    public int totalResults;

    @SerializedName("Search")
    public List<MovieShortInfo> movies;
}
