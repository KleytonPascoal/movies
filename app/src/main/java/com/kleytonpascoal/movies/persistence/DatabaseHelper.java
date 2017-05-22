package com.kleytonpascoal.movies.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kleytonpascoal.movies.model.Movie;

import java.sql.SQLException;

/**
 * Created by kleyton on 09/05/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = TablesNames.Movie.TABLE + ".db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Movie, Long> movieDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getHelper(@NonNull Context context) {
        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public void releaseHelper() {
        OpenHelperManager.releaseHelper();
    }

    public PreparedQuery<Movie> prepareQueryAllMovies() {
        try {
            QueryBuilder<Movie, Long> qb = getMovieDao().queryBuilder();
            return qb.prepare();
        } catch (Exception ex) {
            Log.e(TAG, "Could not get query for all Movies", ex);
        }
        return null;
    }

    public PreparedQuery<Movie> prepareQueryMoviesTitleContains(String filter) {
        try {
            QueryBuilder<Movie, Long> qb = getMovieDao().queryBuilder();
            qb.orderByRaw("title ASC");
            qb.where().like("title", "%" + filter + "%");
            return qb.prepare();
        } catch (Exception ex) {
            Log.e(TAG, "Could not get query for Movies title starts with: " + filter, ex);
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException e) {
            Log.e(TAG, "Could not create new table for Movie", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Movie.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Could not upgrade the table for Movie", e);
        }
    }

    public Dao<Movie, Long> getMovieDao() {
        try {
            if (movieDao == null)
                movieDao = getDao(Movie.class);
            return movieDao;
        } catch (SQLException e) {
            Log.e(TAG, "Could not get the Movie Dao", e);
        }
        return null;
    }
}
