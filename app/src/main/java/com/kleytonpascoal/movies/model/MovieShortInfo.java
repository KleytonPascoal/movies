package com.kleytonpascoal.movies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kleyton on 12/05/17.
 */

public class MovieShortInfo {

    @SerializedName("imdbID")
    public String imdbID;

    @SerializedName("Title")
    public String title;

    @SerializedName("Year")
    public String year;

    @SerializedName("Poster")
    public String poster;

    @SerializedName("Type")
    public String type;

}
