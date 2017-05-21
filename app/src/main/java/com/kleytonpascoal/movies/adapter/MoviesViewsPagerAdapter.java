package com.kleytonpascoal.movies.adapter;

/**
 * Created by kleyton on 10/05/17.
 */

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.fragment.MoviesCarouselPageFragment;
import com.kleytonpascoal.movies.fragment.MoviesGridPageFragment;
import com.kleytonpascoal.movies.fragment.MoviesListPageFragment;
import com.kleytonpascoal.movies.model.Movie;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class MoviesViewsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = MoviesViewsPagerAdapter.class.getSimpleName();

    public interface MoviesViewsSwappableCursor {
        void swapCursor(Cursor cursor, PreparedQuery<Movie> moviePreparedQuery);
    }
    /**
     * The number of pages with movies.
     */
    private static final int NUM_MOVIES_VIEWS_PAGES = 3;

    public static final int MOVIES_CAROUSEL_PAGE_INDEX = 0;
    public static final int MOVIES_LIST_PAGE_INDEX = 1;
    public static final int MOVIES_GRID_PAGE_INDEX = 2;

    private Fragment[] mPages = new Fragment[NUM_MOVIES_VIEWS_PAGES];

    public MoviesViewsPagerAdapter(FragmentManager fm) {
        super(fm);
        mPages[MOVIES_CAROUSEL_PAGE_INDEX] = new MoviesCarouselPageFragment();
        mPages[MOVIES_LIST_PAGE_INDEX] = new MoviesListPageFragment();
        mPages[MOVIES_GRID_PAGE_INDEX] = new MoviesGridPageFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        return mPages[position];
    }

    @Override
    public int getCount() {
        return NUM_MOVIES_VIEWS_PAGES;
    }
}
