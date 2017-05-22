package com.kleytonpascoal.movies.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.model.SearchMovieResult;

/**
 * Created by kleyton on 22/05/17.
 */

public class JsonParserHelper {

    public static SearchMovieResult deserializeSearchResult(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, SearchMovieResult.class);
    }

    public static Movie deserializeMovie(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Movie.class, new MovieJsonDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, Movie.class);
    }
}
