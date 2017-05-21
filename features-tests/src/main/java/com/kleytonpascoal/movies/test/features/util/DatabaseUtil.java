package com.kleytonpascoal.movies.test.features.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kleyton on 19/05/17.
 */

public class DatabaseUtil {

    private static List<Movie> mMovies;

    public static void insertMoviesInDatabase() throws SQLException {
        final Context targetContext = InstrumentationRegistry.getTargetContext();
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(targetContext);
        mMovies = DummyContentJson.createMovieList(targetContext);
        dbHelper.getWritableDatabase().beginTransaction();
        dbHelper.getMovieDao().create(mMovies);
        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();
        dbHelper.releaseHelper();

    }

    public static void removeMoviesFromDatabase() throws SQLException {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(InstrumentationRegistry.getTargetContext());
        dbHelper.getWritableDatabase().beginTransaction();
        dbHelper.getMovieDao().delete(mMovies);
        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();
        dbHelper.releaseHelper();
    }
}
