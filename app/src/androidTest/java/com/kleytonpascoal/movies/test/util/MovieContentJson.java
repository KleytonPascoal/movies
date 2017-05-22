package com.kleytonpascoal.movies.test.util;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.parser.JsonParserHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kleytonpascoal.movies.test.util.StreamUtil.getStringFromStream;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MovieContentJson {

    static final List<Movie> ITEMS = new ArrayList<>();

    static final Map<Long, Movie> ITEM_MAP = new HashMap<>();

    private static Movie[] movies = new Movie[4];

    private static final int COUNT = movies.length;

    public static List<Movie> createMovieList(Context context) {

        try {

            movies[0] = JsonParserHelper.deserializeMovie(getStringFromStream(context.getResources().openRawResource(R.raw.movie_id209112)));
            movies[1] = JsonParserHelper.deserializeMovie(getStringFromStream(context.getResources().openRawResource(R.raw.movie_id415)));
            movies[2] = JsonParserHelper.deserializeMovie(getStringFromStream(context.getResources().openRawResource(R.raw.movie_id142061)));
            movies[3] = JsonParserHelper.deserializeMovie(getStringFromStream(context.getResources().openRawResource(R.raw.movie_id382322)));

            // Add some sample items.
            for (int i = 0; i < COUNT; i++) {
                addItem(createDummyItem(i));
            }
            return ITEMS;
        } catch (Exception e) {}
        return null;
    }

    private static void addItem(Movie item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Movie createDummyItem(int position) {
        final Movie movie = new Movie();
        final int i = position % 4;
        movie.id = movies[i].id;
        movie.title = movies[i].title;
        movie.releaseDate = movies[i].releaseDate;
        movie.overview = movies[i].overview;
        movie.posterPath = movies[i].posterPath;
        movie.genre = movies[i].genre;
        movie.runtime = movies[i].runtime;
        movie.homepage = movies[i].homepage;
        movie.voteCount = movies[i].voteCount;
        movie.production = movies[i].production;
        movie.budget = movies[i].budget;
        movie.language = movies[i].language;
        movie.voteAverage = movies[i].voteAverage;
        movie.status = movies[i].status;

        return movie;
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        return gsonBuilder.create();
    }

    public static Movie createMovie(Context context) throws IOException {
        return JsonParserHelper.deserializeMovie(getStringFromStream(context.getResources().openRawResource(R.raw.movie_id209112)));
    }
}
