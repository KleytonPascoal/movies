package com.kleytonpascoal.movies.test.util;

import com.kleytonpascoal.movies.model.Movie;

/**
 * Created by kleyton on 14/05/17.
 */

public class MovieContent {

    public static Movie createMovie() {
        final long imdbIDValue = 209112;
        final String titleValue = "Star Wars: Episode IV - A New Hope";
        final String yearValue = "1977";
        final String ratedValue = "PG";
        final String releasedValue = "25 May 1977";
        final String runtimeValue = "121 min";
        final String genreValue = "Action, Adventure, Fantasy";
        final String directorValue = "George Lucas";
        final String writerValue = "George Lucas";
        final String actorsValue = "Mark Hamill, Harrison Ford, Carrie Fisher, Peter Cushing";
        final String plotValue = "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, " +
                "a wookiee and two droids to save the galaxy from the Empire's world-destroying battle-station, " +
                "while also attempting to rescue Princess Leia from the evil Darth Vader.";
        final String languageValue = "English";
        final String countryValue = "USA";
        final String awardsValue = "Won 6 Oscars. Another 50 wins & 28 nominations.";
        final String posterValue = "https://images-na.ssl-images-amazon.com/images/M/" +
                "MV5BYzQ2OTk4N2QtOGQwNy00MmI3LWEwNmEtOTk0OTY3NDk2MGJkL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_SX300.jpg";
        final String metascoreValue = "92";
        final String imdbRatingValue = "8.7";
        final String imdbVotes = "963,318";
        final String typeValue = "movie";
        final String dvdValue = "21 Sep 2004";
        final String boxOfficeValue = "$167,007,184.00";
        final String productionValue = "20th Century Fox";
        final String websiteValue = "http://www.starwars.com/episode-iv/";
        final boolean responseValue = true;
        final boolean favoriteValue = true;

        final Movie movie = new Movie();
        movie.id = imdbIDValue;
        movie.title = titleValue;
        movie.releaseDate = yearValue;
        movie.status = releasedValue;
        movie.runtime = runtimeValue;
        movie.genre = genreValue;
        movie.overview = plotValue;
        movie.language = languageValue;
        movie.posterPath = posterValue;
        movie.voteAverage = imdbRatingValue;
        movie.voteCount = imdbVotes;
        movie.budget = boxOfficeValue;
        movie.production = productionValue;
        movie.homepage = websiteValue;

        return movie;
    }
}
