package com.kleytonpascoal.movies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kleyton on 12/05/17.
 */

public class MovieShortInfo {

    @SerializedName("id")
    public long id;

    @SerializedName("title")
    public String title;

    @SerializedName("release_date")
    public String releaseDate;

}
