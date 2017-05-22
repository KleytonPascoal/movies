package com.kleytonpascoal.movies.test.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;
import com.kleytonpascoal.movies.test.util.DummyContentJson;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    @Test
    public void persistMovie() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();

        DatabaseHelper helper = DatabaseHelper.getHelper(appContext);
        final Movie movie = new Movie();
        movie.id = 1;
        movie.title = "Movie 1";
        assertEquals(1, helper.getMovieDao().create(movie));
        helper.releaseHelper();


        helper = DatabaseHelper.getHelper(appContext);
        final Movie movieSaved = helper.getMovieDao().queryForId(1L);
        assertNotNull(movieSaved);
        helper.releaseHelper();


        helper = DatabaseHelper.getHelper(appContext);
        assertEquals(1, helper.getMovieDao().deleteById(1L));
        helper.releaseHelper();


        helper = DatabaseHelper.getHelper(appContext);
        final Movie movieDeleted = helper.getMovieDao().queryForId(1L);
        assertNull(movieDeleted);
        helper.releaseHelper();

    }
}