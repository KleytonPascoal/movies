package com.kleytonpascoal.movies.helper;

import java.util.Locale;

/**
 * Created by kleyton on 22/05/17.
 */

public class UrlBuilder {

    public static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?" +
            "api_key=2ee1ea7c6399c23fa81d4de98a88452c&" +
            "language=en-US&" +
            "include_adult=false&";

    public static final String MOVIE_ID_URL = "https://api.themoviedb.org/3/movie/%d?" +
            "api_key=2ee1ea7c6399c23fa81d4de98a88452c&language=en-US";

    public static final String MOVIE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    public static final String buildSearch(String query, int pageIndex) {
        return SEARCH_URL + "query=" + query.replace(" ", "+") + "&page=" + pageIndex;
    }

    public static final String buildMovieById(long id) {
        return String.format(Locale.US, MOVIE_ID_URL, id);
    }

    public static String buildImageUrl(String path) {
        return MOVIE_IMAGE_URL + path;
    }
}
