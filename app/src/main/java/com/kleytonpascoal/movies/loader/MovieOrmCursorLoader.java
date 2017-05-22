package com.kleytonpascoal.movies.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.model.Movie;

/**
 * Created by kleyton on 09/05/17.
 */

public class MovieOrmCursorLoader extends CursorLoader {

    private Dao<Movie, Long> mMovieDao;
    private PreparedQuery<Movie> mPrepareQuery;

    public MovieOrmCursorLoader(Context context,
                                Dao<Movie, Long> movieDao,
                                PreparedQuery<Movie> preparedQuery) {
        super(context);
        mMovieDao = movieDao;
        mPrepareQuery = preparedQuery;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        try {
            CloseableIterator<Movie> iterator = mMovieDao.iterator(mPrepareQuery);
            AndroidDatabaseResults results =
                    (AndroidDatabaseResults)iterator.getRawResults();
            cursor = results.getRawCursor();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return cursor;
    }
}
