package com.kleytonpascoal.movies.test.features.util;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContentJson {

    static final List<Movie> ITEMS = new ArrayList<>();

    static final Map<String, Movie> ITEM_MAP = new HashMap<>();

    private static Movie[] movies = new Movie[4];

    private static final int COUNT = movies.length;

    public static List<Movie> createMovieList(Context context) {

        try {

            final Gson gson = createGson();
            movies[0] = gson.fromJson(getStringFromStream(context.getResources().openRawResource(R.raw.search_title_age_of_ice)), Movie.class);
            movies[1] = gson.fromJson(getStringFromStream(context.getResources().openRawResource(R.raw.search_title_casino_royale)), Movie.class);
            movies[2] = gson.fromJson(getStringFromStream(context.getResources().openRawResource(R.raw.search_title_star_wars)), Movie.class);
            movies[3] = gson.fromJson(getStringFromStream(context.getResources().openRawResource(R.raw.search_title_titanic)), Movie.class);

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
        ITEM_MAP.put(item.imdbID, item);
    }

    private static Movie createDummyItem(int position) {
        final Movie movie = new Movie();
        final int i = position % 4;
        movie.imdbID = movies[i].imdbID;
        movie.title = movies[i].title;
        movie.year = movies[i].year;
        movie.plot = movies[i].plot;
        movie.poster = movies[i].poster;
        movie.genre = movies[i].genre;
        movie.runtime = movies[i].runtime;
        movie.website = movies[i].website;
        movie.awards = movies[i].awards;
        movie.imdbVotes = movies[i].imdbVotes;
        movie.production = movies[i].production;
        movie.actors = movies[i].actors;
        movie.boxOffice = movies[i].boxOffice;
        movie.language = movies[i].language;
        movie.country = movies[i].country;
        movie.dvd = movies[i].dvd;
        movie.metascore = movies[i].metascore;
        movie.director = movies[i].director;
        movie.rated = movies[i].rated;
        //movie.ratings = movies[i].ratings;
        movie.imdbRating = movies[i].imdbRating;
        movie.type = movies[i].type;
        movie.released = movies[i].released;
        movie.writer = movies[i].writer;
        movie.response = movies[i].response;

        return movie;
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        return gsonBuilder.create();
    }

    private static String getStringFromStream(InputStream inputStream) throws IOException {
        final BufferedInputStream bufferInputStream = new BufferedInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int result = bufferInputStream.read();
        while (result != -1) {
            outputStream.write((byte) result);
            result = bufferInputStream.read();
        }
        return outputStream.toString("UTF-8");
    }
}
