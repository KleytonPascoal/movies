package com.kleytonpascoal.movies.persistence;

/**
 * Created by kleyton on 11/05/17.
 */

public class TablesNames {
    public interface Movie {
        String TABLE = "movies";
        String ID = "id";
        String TITLE = "title";
        String RELEASE_DATE = "releaseDate";
        String RATED = "rated";
        String STATUS = "status";
        String RUNTIME = "runtime";
        String GENRE = "genre";
        String OVERVIEW = "overview";
        String LANGUAGE = "language";
        String COUNTRY = "country";
        String AWARDS = "awards";
        String POSTER_PATH = "posterPath";
        String METASCORE = "metascore";
        String VOTE_AVERAGE = "voteAverage";
        String IMDBVOTES = "voteCount";
        String TYPE = "type";
        String DVD = "dvd";
        String BUDGET = "budget";
        String PRODUCTION = "production";
        String HOMEPAGE = "homepage";
    }
}
