package com.kleytonpascoal.movies.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;

import java.sql.SQLException;

/**
 * Created by kleyton on 09/05/17.
 */

public class MoviePersistenceService extends IntentService {

    private static final String TAG = MoviePersistenceService.class.getSimpleName();
    private static final String PKG = "com.kleytonpascoal.movies.service";

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SAVE_OR_UPDATE_MOVIE = PKG + ".action.SAVE_OR_UPDATE_MOVIE";
    public static final String ACTION_SAVE_OR_UPDATE_MOVIE_DONE = PKG + ".action.SAVE_OR_UPDATE_MOVIE_DONE";
    public static final String ACTION_DELETE_MOVIE = PKG + ".action.DELETE_MOVIE";
    public static final String ACTION_DELETE_MOVIE_DONE = PKG + ".action.DELETE_MOVIE_DONE";
    public static final String EXTRA_SAVED_OR_UPDATED_MOVIE_STATUS = PKG + ".extra.EXTRA_SAVED_OR_UPDATED_MOVIE_STATUS";
    public static final String EXTRA_MOVIE_PARAM = PKG + ".extra.MOVIE_PARAM";
    public static final String EXTRA_DELETED_MOVIE_STATUS = PKG + ".extra.EXTRA_DELETED_MOVIE_STATUS";

    private DatabaseHelper dbHelper;

    public MoviePersistenceService() {
        super(MoviePersistenceService.class.getName());
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSaveOrUpdateMovie(Context context, Movie movie) {
        Log.d(TAG, "startActionSaveOrUpdateMovie");
        Intent intent = new Intent(context, MoviePersistenceService.class);
        intent.setAction(ACTION_SAVE_OR_UPDATE_MOVIE);
        intent.putExtra(EXTRA_MOVIE_PARAM, movie);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDeleteMovie(Context context, Movie movie) {
        Log.d(TAG, "startActionDeleteMovie");
        Intent intent = new Intent(context, MoviePersistenceService.class);
        intent.setAction(ACTION_DELETE_MOVIE);
        intent.putExtra(EXTRA_MOVIE_PARAM, movie);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        dbHelper = DatabaseHelper.getHelper(getBaseContext());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_OR_UPDATE_MOVIE.equals(action)) {
                final Movie movie = intent.getParcelableExtra(EXTRA_MOVIE_PARAM);
                handleActionSaveOrUpdate(movie);
            } else if (ACTION_DELETE_MOVIE.equals(action)) {
                final Movie movie = intent.getParcelableExtra(EXTRA_MOVIE_PARAM);
                handleActionDelete(movie);
            }
        }
        dbHelper.releaseHelper();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSaveOrUpdate(Movie movie) {
        boolean movieStatus = false;
        try {
            dbHelper.getWritableDatabase().beginTransaction();
            final Dao.CreateOrUpdateStatus createOrUpdateStatus = dbHelper.getMovieDao().createOrUpdate(movie);
            movieStatus = createOrUpdateStatus.isCreated() || createOrUpdateStatus.isUpdated();
            dbHelper.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e(TAG, exc.getMessage());
        } finally {
            dbHelper.getWritableDatabase().endTransaction();
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(ACTION_SAVE_OR_UPDATE_MOVIE_DONE).
                        putExtra(EXTRA_SAVED_OR_UPDATED_MOVIE_STATUS, movieStatus).
                        putExtra(EXTRA_MOVIE_PARAM, movie));

        Log.d(TAG, "handleActionSaveOrUpdate done!");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDelete(Movie movie) {
        int deleted = 0;
        try {
            dbHelper.getWritableDatabase().beginTransaction();
            deleted = dbHelper.getMovieDao().delete(movie);
            dbHelper.getWritableDatabase().setTransactionSuccessful();
        } catch (SQLException exc) {
            Log.e(TAG, exc.getMessage());
        } finally {
            dbHelper.getWritableDatabase().endTransaction();
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(ACTION_DELETE_MOVIE_DONE).
                        putExtra(EXTRA_DELETED_MOVIE_STATUS, deleted == 1).
                        putExtra(EXTRA_MOVIE_PARAM, movie));

        Log.d(TAG, "handleActionDelete done!");
    }
}
